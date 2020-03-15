package com.gfutac.rest.mapping;

import com.gfutac.model.Book;
import com.gfutac.rest.dto.BookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    Book toEntity(BookDTO dto);
    BookDTO toDTO(Book entity);
}
