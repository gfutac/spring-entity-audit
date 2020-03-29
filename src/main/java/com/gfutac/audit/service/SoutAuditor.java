package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoutAuditor implements Auditor {

    @Autowired
    private ObjectWriter entityWriter;

    @Override
    public void audit(AuditEntity auditEntity) {
        try {
            var message = this.entityWriter.writeValueAsString(auditEntity);
            log.info("" + message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity: {} {} with error {}", auditEntity.getEntityStateChangeType(), auditEntity.getEntity(), e);
        }
    }
}
