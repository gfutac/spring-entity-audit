package com.gfutac.rest.mapping;

import com.gfutac.model.Author;
import com.gfutac.rest.dto.AuthorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author toEntity(AuthorDTO dto);
    AuthorDTO toDTO(Author entity);
}
