package com.gfutac.service;

import com.gfutac.exceptions.NoSuchEntityException;
import com.gfutac.model.repositories.BookChapterRepository;
import com.gfutac.model.repositories.BookRepository;
import com.gfutac.rest.dto.BookChapterDTO;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.mapping.BookChapterMapper;
import com.gfutac.rest.mapping.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookChapterRepository bookChapterRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookChapterMapper bookChapterMapper;

    public BookDTO updateBook(long bookId, BookDTO bookDTO) {
        var book = this.bookRepository.findById(bookId).orElseThrow(() -> new NoSuchEntityException(bookId, "Book not found"));
        book.setName(bookDTO.getName());
        book = this.bookRepository.saveAndFlush(book);

        return this.bookMapper.toDTO(book);
    }

    public BookChapterDTO addChapterToBook(long bookId, BookChapterDTO chapterDTO) {
        var book = this.bookRepository.findById(bookId).orElseThrow(() -> new NoSuchEntityException(bookId, "Book not found"));
        var chapter = this.bookChapterMapper.toEntity(chapterDTO);
        chapter.setBook(book);
        chapter = this.bookChapterRepository.saveAndFlush(chapter);

        return this.bookChapterMapper.toDTO(chapter);
    }
}
