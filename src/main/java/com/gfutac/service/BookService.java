package com.gfutac.service;

import com.gfutac.exceptions.NoSuchEntityException;
import com.gfutac.model.repositories.BookRepository;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.mapping.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    public BookDTO updateBook(long bookId, BookDTO bookDTO) {
        var book = this.bookRepository.findById(bookId).orElseThrow(() -> new NoSuchEntityException(bookId, "Book not found"));
        book.setName(bookDTO.getName());
        book = this.bookRepository.saveAndFlush(book);

        return this.bookMapper.toDTO(book);
    }
}
