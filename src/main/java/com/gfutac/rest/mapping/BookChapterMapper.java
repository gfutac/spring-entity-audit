package com.gfutac.rest.mapping;

import com.gfutac.model.BookChapter;
import com.gfutac.rest.dto.BookChapterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class BookChapterMapper implements EntityToDtoMapper<BookChapter, BookChapterDTO> {

    @Override
    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "book.name", target = "book"),
            @Mapping(source = "book.author.name", target = "author")
    })
    public abstract BookChapterDTO toDTO(BookChapter bookChapter);

    @Override
    public BookChapter toEntity(BookChapterDTO bookChapterDTO) {
        return null;
    }
}
