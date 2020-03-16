package com.gfutac.rest.mapping;

public interface EntityToDtoMapper<TEntity, TDto> {
    TDto toDTO(TEntity entity);
    TEntity toEntity(TDto dto);
}
