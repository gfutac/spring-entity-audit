package com.gfutac.rest;

import com.gfutac.audit.model.AuditEntity;
import com.gfutac.audit.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@RestController
@Slf4j
public class BaseAuditController<T> {

    @Autowired
    private AuditService auditService;

    @GetMapping(value = "/entry/{key}")
    public List<AuditEntity> getAuditEntriesForEntity(@PathVariable Object key) {

        try {
            var type = this.getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                type = ((ParameterizedType) type).getActualTypeArguments()[0];
                var clazz = Class.forName(type.getTypeName());
                var entity = clazz.getDeclaredConstructor().newInstance();

                return this.auditService.getAuditEntriesForEntity(entity, key);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            log.error("Died.", e);
        }

        return null;
    }
}
