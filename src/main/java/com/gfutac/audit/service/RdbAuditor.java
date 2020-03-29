package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.model.repositories.AuditEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RdbAuditor implements Auditor {

    @Autowired
    private ObjectWriter entityWriter;

    @Autowired
    private AuditEntityRepository auditEntityRepository;

    @Override
    public void audit(AuditEntity message) {
        // i really need to find better names
        var auditEntity = new com.gfutac.model.AuditEntity();

        try {
            auditEntity
                    .setEntityType(message.getEntityType().getName())
                    .setEntityKey(message.getEntityKey().toString())
                    .setEntityStateChangeTime(message.getEntityStateChangeTime())
                    .setEntityStateChangeType(message.getEntityStateChangeType())
                    .setEntity(this.entityWriter.writeValueAsString(message.getEntity()));

            this.auditEntityRepository.saveAndFlush(auditEntity);

        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity: {} {} with error {}", auditEntity.getEntityStateChangeType(), auditEntity.getEntity(), e);
        }
    }
}
