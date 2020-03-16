package com.gfutac.rest.mapping;

import com.gfutac.model.Book;
import com.gfutac.rest.dto.BookDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BookMapper implements EntityToDtoMapper<Book, BookDTO>{
}
