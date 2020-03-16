package com.gfutac.audit.model;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditEntityMapper {
    AuditEntityDTO toDTO(AuditEntity entity);
}
