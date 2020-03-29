package com.gfutac.model;

import com.gfutac.audit.model.EntityStateChangeType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "AuditEntity")
@Data
@Accessors(chain = true)
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AuditEntityID")
    private long auditEntityId;

    @Column(name = "EntityType")
    private String entityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "EntityStateChangeType")
    private EntityStateChangeType entityStateChangeType;

    @Column(name = "EntityStateChangeTime")
    private Instant entityStateChangeTime;

    @Column(name = "Entity")
    private String entity;

    @Column(name = "EntityKey")
    private String entityKey;
}
