package com.gfutac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public void auditSavedObject(Object savedObject) {
        try {
            var json = this.objectMapper.writeValueAsString(savedObject);
            log.info(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
