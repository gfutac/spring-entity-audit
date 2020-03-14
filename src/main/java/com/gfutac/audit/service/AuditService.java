package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.model.EntityStateChangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AuditService {

    private ObjectWriter entityWriter;
    private Auditor auditor;
    private String auditLogEndpoint;

    public AuditService(@Autowired ObjectWriter entityWriter, @Autowired AuditorFactoryBean auditorFactoryBean, @Value("${auditor.audit-log.endpoint}") String auditLogEndpoint) {
        this.entityWriter = entityWriter;
        this.auditor = auditorFactoryBean.getObject();
        this.auditLogEndpoint = auditLogEndpoint;
    }

    @Async("auditThreadPool")
    public void auditChangedEntity(Object savedObject, Object entityKey, EntityStateChangeType changeType) {
        try {
            var audit = new AuditEntity()
                    .setEntityType(savedObject.getClass())
                    .setEntityStateChangeType(changeType)
                    .setEntityStateChangeTime(Instant.now())
                    .setEntity(savedObject)
                    .setEntityKey(entityKey);

            var json = this.entityWriter.writeValueAsString(audit);
            this.auditor.audit(json);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity: {} {} with error {}", changeType, savedObject, e);
        }
    }

    public List<AuditEntity> getAuditEntriesForEntity(Object entity, Object key) {
        var type = entity.getClass().getName();

        var queryParamBuilder = UriComponentsBuilder.fromUriString(this.auditLogEndpoint)
                .queryParam("entityType", type)
                .queryParam("entityKey", key);

        var restResult = new RestTemplate().getForEntity(queryParamBuilder.buildAndExpand(this.auditLogEndpoint).toUri(), AuditEntity[].class);
        if (restResult.getBody() != null) {
            return Arrays.asList(restResult.getBody());
        }

        return null;
    }
}
