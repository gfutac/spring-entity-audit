package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.model.EntityStateChangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.Instant;

@Service
@Slf4j
public class AuditService {

    private ObjectWriter entityWriter;
    private Auditor auditor;

    public AuditService(@Autowired ObjectWriter entityWriter, @Autowired AuditorFactoryBean auditorFactoryBean) {
        this.entityWriter = entityWriter;
        this.auditor = auditorFactoryBean.getObject();
    }

    @Async("auditThreadPool")
    public void auditChangedEntity(Object savedObject, Serializable entityKey, EntityStateChangeType changeType) {
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
}
