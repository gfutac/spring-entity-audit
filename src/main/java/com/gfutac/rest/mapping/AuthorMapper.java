package com.gfutac.rest.mapping;

import com.gfutac.model.Author;
import com.gfutac.rest.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuthorMapper implements EntityToDtoMapper<Author, AuthorDTO>{

}
