package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.model.AuditableEntity;
import com.gfutac.audit.model.EntityStateChangeType;
import com.gfutac.jms.AuditTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class AuditService {

    private static final String entityColumnFilterName = "ENTITY_COLUMN_FILTER";

    private ObjectWriter writer;

    private AuditTopic auditTopic;

    public AuditService(@Autowired PropertyFilter entityColumnFilter, @Autowired AuditTopic auditTopic) {
        var mapper = new ObjectMapper();
        this.auditTopic = auditTopic;

        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(entityColumnFilterName, entityColumnFilter);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findFilterId(Annotated a) {
                AuditableEntity ann = this._findAnnotation(a, AuditableEntity.class);
                if (ann != null) {
                    return entityColumnFilterName;
                }

                return null;
            }
        });

        this.writer = mapper.writer(filters);
    }

    public void auditChangedEntity(Object savedObject, Object entityKey, EntityStateChangeType changeType) {
        try {
            var audit = new AuditEntity()
                    .setEntityType(savedObject.getClass())
                    .setEntityStateChangeType(changeType)
                    .setEntityStateChangeTime(Instant.now())
                    .setEntity(savedObject)
                    .setEntityKey(entityKey);

            var json = this.writer.writeValueAsString(audit);
            this.auditTopic.send(json);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity: {} {}", changeType, savedObject);
        }
    }
}
