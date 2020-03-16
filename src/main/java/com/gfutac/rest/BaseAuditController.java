package com.gfutac.rest;

import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@RestController
@Slf4j
public class BaseAuditController<T> {

    @Autowired
    private AuditService auditService;

    /**
     * Endpoint that will be be inherited in all controllers that extend @{@link BaseAuditController} with generic type of @{@link javax.persistence.Entity}
     * for which history records will be fetched from audit-log-service
     * @param key Primary key of object for which history records will be retreived
     * @return List of @{@link AuditEntity} records
     */
    @GetMapping(value = "/audit-log/{key}")
    public List<AuditEntity> getAuditEntriesForEntity(@PathVariable Object key) {

        try {
            var type = this.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                var clazz = Class.forName(type.getTypeName());

                return this.auditService.getAuditEntriesForEntity(clazz, key);
            }
        } catch (ClassNotFoundException e) {
            log.error("Died.", e);
        }

        return null;
    }
}
