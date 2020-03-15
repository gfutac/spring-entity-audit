package com.gfutac.service;

import com.gfutac.exceptions.NoSuchEntityException;
import com.gfutac.repositories.AuthorRepository;
import com.gfutac.repositories.BookRepository;
import com.gfutac.rest.dto.AuthorDTO;
import com.gfutac.rest.mapping.AuthorMapper;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.mapping.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public AuthorDTO addAuthor(AuthorDTO dto) {
        var author = AuthorMapper.INSTANCE.toEntity(dto);
        author = this.authorRepository.saveAndFlush(author);
        return AuthorMapper.INSTANCE.toDTO(author);
    }

    public AuthorDTO getAuthor(long authorId) {
        var author = this.authorRepository.findById(authorId).orElseThrow(() -> new NoSuchEntityException(authorId, "Author not found"));;
        return AuthorMapper.INSTANCE.toDTO(author);
    }

    public BookDTO addBookToAuthor(long authorId, BookDTO bookDTO) {
        var author = this.authorRepository.findById(authorId).orElseThrow(() -> new NoSuchEntityException(authorId, "Author not found"));;
        var book = BookMapper.INSTANCE.toEntity(bookDTO);

        book.setAuthor(author);
        book = this.bookRepository.saveAndFlush(book);
        return BookMapper.INSTANCE.toDTO(book);
    }
}
