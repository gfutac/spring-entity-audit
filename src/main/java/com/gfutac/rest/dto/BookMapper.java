package com.gfutac.rest.dto;

import com.gfutac.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toEntity(BookDTO dto);
    BookDTO toDTO(Book entity);
}
