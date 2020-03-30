package com.gfutac.audit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.model.AuditEntityDTO;
import com.gfutac.audit.model.AuditEntityMapper;
import com.gfutac.audit.model.EntityStateChangeType;
import com.gfutac.rest.mapping.EntityToDtoMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditService {

    @Autowired
    private EntityManager em;

    @Autowired
    private EntityToDtoMapperFactory entityToDtoMapperFactory;

    @Autowired
    private AuditEntityMapper auditEntityMapper;

    private ObjectWriter entityWriter;
    private Auditor auditor;
    private String auditLogEndpoint;

    public AuditService(@Autowired ObjectWriter entityWriter, @Autowired AuditorFactoryBean auditorFactoryBean, @Value("${auditor.audit-log.endpoint}") String auditLogEndpoint) {
        this.entityWriter = entityWriter;
        this.auditor = auditorFactoryBean.getObject();
        this.auditLogEndpoint = auditLogEndpoint;
    }

    /**
     * Audits saved object. Only inserts and updates are tracked here.
     *
     * @param savedObject Hibernate entity that was saved (inserted or updated) to database.
     * @param entityKey   Primary key of saved entity.
     * @param changeType  Type of change, insert or update
     */
    @Async("auditThreadPool")
    @Transactional
    public void auditChangedEntity(Object savedObject, Object entityKey, EntityStateChangeType changeType) {
        var auditEntity = new AuditEntity()
                .setEntityType(savedObject.getClass())
                .setEntityStateChangeType(changeType)
                .setEntityStateChangeTime(Instant.now())
                .setEntity(savedObject)
                .setEntityKey(entityKey);

        this.auditor.audit(auditEntity);
    }

    /**
     * Gets history records from audit-log-service for given
     *
     * @param entityType Type of entity to be searched for.
     * @param key        Primary key of the entity to be searched for.
     * @return List of {@link AuditEntity} records.
     */
    public List<AuditEntityDTO> getAuditEntriesForEntity(Class<?> entityType, Object key) {

        var queryParamBuilder = UriComponentsBuilder.fromUriString(this.auditLogEndpoint)
                .queryParam("entityType", entityType.getName())
                .queryParam("entityKey", key);

        var restResult = new RestTemplate().getForEntity(queryParamBuilder.buildAndExpand(this.auditLogEndpoint).toUri(), AuditEntity[].class);
        if (restResult.getBody() != null) {
            var tmp = Arrays.asList(restResult.getBody());
            var result = tmp.stream().map(ae -> {
                var deserialized = this.deserializeAuditEntityToEntity(ae);
                var dto = this.entityToDtoMapperFactory.getMapper(ae.getEntityType()).toDTO(deserialized);
                ae.setEntity(dto);
                return this.auditEntityMapper.toDTO(ae);
            }).collect(Collectors.toList());

            return result;
        }

        return null;
    }

    /**
     * Deserializes AuditEntity fetched from audit-log-service to specific @{@link javax.persistence.Entity}
     *
     * @param auditEntity record from audit-log-service
     * @return Deserialized @{@link javax.persistence.Entity}
     */
    private Object deserializeAuditEntityToEntity(AuditEntity auditEntity) {
        Object result = null;

        var entityType = auditEntity.getEntityType();

        // Create "empty" instance where result will be stored. Since result will be hibernate entity we can be sure no args constructor exist.
        // since it is a hibernate requirement. hibernate relies on newInstance() for creation of new instances too.
        try {
            result = entityType.getDeclaredConstructor().newInstance();

            var fields = entityType.getDeclaredFields();

            // Object will be serialized as Map when coming in JSON from rest calls
            if (auditEntity.getEntity() instanceof Map) {
                var asMap = (Map<?, ?>) auditEntity.getEntity();

                var mapper = new ObjectMapper();
                for (var field : fields) {
                    try {
                        // if we want to use reflection to set fields this is required
                        field.setAccessible(true);

                        // there are two cases
                        // fields annotated with javax.persistence.Column are most often basic types
                        if (field.isAnnotationPresent(javax.persistence.Column.class)) {
                            // if it is a string just read
                            if (field.getType() == java.lang.String.class) {
                                var value = asMap.get(field.getName());
                                field.set(result, value);
                            } else {
                                // otherwise try to convert it to appropriate type
                                var value = mapper.convertValue(asMap.get(field.getName()), field.getType());
                                field.set(result, value);
                            }
                        } else if (field.isAnnotationPresent(javax.persistence.JoinColumn.class)) {
                            // second case is for javax.persistence.JoinColumn
                            // here we have referenced entity. we first extract name of the primary key of referenced entity
                            var ann = field.getAnnotation(javax.persistence.JoinColumn.class);
                            var refColumnKeyName = ann.name();
                            // it's type
                            var refColumnKeyType = field.getType().getDeclaredField(refColumnKeyName).getType();
                            // and then we can get actual value of the PK of the referenced entity
                            var refColumnPK = mapper.convertValue(asMap.get(refColumnKeyName), refColumnKeyType);

                            // "low-level" call to EntityManager to find desired entity (no spring data here :))
                            var refEntity = em.find(field.getType(), refColumnPK);
                            field.set(result, refEntity);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error("Failed to construct value for " + field.getName() + " from type " + field.getType(), e);
                    }
                }
            }

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Failed to construct object of type " + entityType, e);
        }

        return result;
    }
}
