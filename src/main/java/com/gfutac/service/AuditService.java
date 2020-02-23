package com.gfutac.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gfutac.audit.AuditEntity;
import com.gfutac.audit.AuditEntityColumnPropertyFilterConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    private ObjectWriter writer;

    public AuditService(@Autowired PropertyFilter entityColumnFilter) {
        var mapper = new ObjectMapper();

        SimpleFilterProvider filters = new SimpleFilterProvider().addFilter(AuditEntityColumnPropertyFilterConfiguration.entityColumnFilterName, entityColumnFilter);
        this.writer = mapper.writer(filters);
    }

    public void auditSavedObject(Object savedObject) {
        try {
            var audit = new AuditEntity()
                    .setAuditEntityType(savedObject.getClass())
                    .setEntity(savedObject);
            var json = this.writer.writeValueAsString(audit);
            log.info(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
