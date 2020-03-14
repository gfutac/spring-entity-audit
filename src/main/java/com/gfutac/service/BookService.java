package com.gfutac.service;

import com.gfutac.repositories.BookRepository;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.dto.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookDTO updateBook(long bookId, BookDTO bookDTO) {
        var book = BookMapper.INSTANCE.toEntity(bookDTO);
        book.setBookId(bookId);
        book = this.bookRepository.saveAndFlush(book);

        return BookMapper.INSTANCE.toDTO(book);
    }
}
