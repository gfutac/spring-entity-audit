package com.gfutac.audit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
public class AuditEntity {
    private Class<?> entityType;
    private EntityStateChangeType entityStateChangeType;
    private Instant entityStateChangeTime;
    private Object entity;
    private Serializable entityKey;
}
