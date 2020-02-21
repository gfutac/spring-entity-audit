package com.gfutac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfutac.audit.AuditEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    private ObjectMapper objectMapper;

    public AuditService() {
        this.objectMapper = new ObjectMapper();
    }

    public void auditSavedObject(Object savedObject) {
        try {
            var audit = new AuditEntity()
                    .setAuditEntityType(savedObject.getClass())
                    .setEntity(savedObject);
            var json = this.objectMapper.writeValueAsString(audit);
            log.info(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
