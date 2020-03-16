package com.gfutac.rest.mapping;

import com.gfutac.model.BookChapter;
import com.gfutac.rest.dto.BookChapterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BookChapterMapper implements EntityToDtoMapper<BookChapter, BookChapterDTO>{
}
