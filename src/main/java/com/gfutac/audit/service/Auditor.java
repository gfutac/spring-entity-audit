package com.gfutac.audit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gfutac.audit.model.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Auditor {

    protected ObjectWriter entityWriter;

    public abstract void audit(AuditEntity message);

    protected String serializeEntity(Object entity) throws JsonProcessingException {
        return this.entityWriter.writeValueAsString(entity);
    }

    @Autowired
    public final void setEntityWriter(ObjectWriter entityWriter) {
        this.entityWriter = entityWriter;
    }
}
