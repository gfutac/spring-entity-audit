package com.gfutac.audit;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AuditEntity {
    private Class<?> auditEntityType;
    private Object entity;
}
