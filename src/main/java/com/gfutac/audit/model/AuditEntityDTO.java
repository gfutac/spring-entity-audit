package com.gfutac.audit.model;

import lombok.Data;

import java.time.Instant;

@Data
public class AuditEntityDTO {
    private String entityStateChangeType;
    private Instant entityStateChangeTime;
    private Object entity;
}
