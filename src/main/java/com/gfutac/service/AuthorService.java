package com.gfutac.service;

import com.gfutac.exceptions.NoSuchEntityException;
import com.gfutac.model.repositories.AuthorRepository;
import com.gfutac.model.repositories.BookRepository;
import com.gfutac.rest.dto.AuthorDTO;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.mapping.AuthorMapper;
import com.gfutac.rest.mapping.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookMapper bookMapper;

    public AuthorDTO addAuthor(AuthorDTO dto) {
        var author = this.authorMapper.toEntity(dto);
        author = this.authorRepository.saveAndFlush(author);
        return this.authorMapper.toDTO(author);
    }

    public AuthorDTO getAuthor(long authorId) {
        var author = this.authorRepository.findById(authorId).orElseThrow(() -> new NoSuchEntityException(authorId, "Author not found"));;
        return this.authorMapper.toDTO(author);
    }

    public BookDTO addBookToAuthor(long authorId, BookDTO bookDTO) {
        var author = this.authorRepository.findById(authorId).orElseThrow(() -> new NoSuchEntityException(authorId, "Author not found"));;
        var book = this.bookMapper.toEntity(bookDTO);

        book.setAuthor(author);
        book = this.bookRepository.saveAndFlush(book);
        return this.bookMapper.toDTO(book);
    }
}
