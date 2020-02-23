package com.gfutac.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.gfutac.audit.AuditEntity;
import com.gfutac.audit.AuditableEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditService {

    private static final String entityColumnFilterName = "ENTITY_COLUMN_FILTER";

    private ObjectWriter writer;

    public AuditService(@Autowired PropertyFilter entityColumnFilter) {
        var mapper = new ObjectMapper();

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
