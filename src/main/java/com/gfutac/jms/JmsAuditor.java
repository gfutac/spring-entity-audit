package com.gfutac.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.service.Auditor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsAuditor extends Auditor {

    @Autowired
    private AuditTopic auditTopic;

    @Override
    public void audit(AuditEntity auditEntity) {
        try {
            var message = this.serializeEntity(auditEntity);
            this.auditTopic.send(message);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize entity: {} {} with error {}", auditEntity.getEntityStateChangeType(), auditEntity.getEntity(), e);
        }
    }
}
