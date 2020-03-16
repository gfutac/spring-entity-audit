package com.gfutac.services;

import com.gfutac.config.AbstractIntegrationTest;
import com.gfutac.model.Author;
import com.gfutac.model.Book;
import com.gfutac.model.repositories.AuthorRepository;
import com.gfutac.model.repositories.BookRepository;
import com.gfutac.rest.dto.AuthorDTO;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.rest.mapping.AuthorMapper;
import com.gfutac.rest.mapping.BookMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class AuditTests extends AbstractIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void getAllUsers() {
        var authors = this.authorRepository.findAll();
        Assert.assertNotEquals(0, authors.size());
    }

    @Test
    public void getBooksByJohnDoe() {
        var authorName = "John Doe";
        var books = this.bookRepository.getAllByAuthor_Name(authorName);
        Assert.assertNotEquals(0, books.size());
    }

    @Test
    public void canAddAuthor() {
        var authorName = "JRR Tolkien";

        var author = new Author().setName(authorName);
        this.authorRepository.save(author);

        Assert.assertTrue(this.authorRepository.existsByName(authorName));
    }

    @Test
    public void canSaveAndUpdate() {

        var authorName = "JRR Tolkien";
        var author = new Author().setName(authorName);
        author = this.authorRepository.save(author);

        var newAuthorName = "Tolken";
        author.setName(newAuthorName);

        this.authorRepository.flush();

        Assert.assertTrue(this.authorRepository.existsByName(newAuthorName));
    }

    @Test
    public void authorHasBookAfterAdding() {
        var authorName = "JRR Tolkien";
        var bookName = "LOTR";

        var book = new Book().setName(bookName);
        this.bookRepository.save(book);

        var author = new Author()
                .setName(authorName);
        author = this.authorRepository.save(author);
        author.setBooks(Collections.singletonList(book));
        book.setAuthor(author);

        this.bookRepository.flush();
        this.authorRepository.flush();

        Assert.assertTrue(this.bookRepository.existsByName(bookName));
        Assert.assertTrue(this.bookRepository.existsById(book.getBookId()));
        Assert.assertEquals(author.getAuthorId(), this.bookRepository.findById(book.getBookId()).get().getAuthor().getAuthorId());
        Assert.assertNotEquals(0, author.getBooks().size());
    }

    @Test
    public void bookWillBeAudited() {
        var bookName = "My greatest work 2";

        var book = new Book()
                .setAuthor(new Author().setAuthorId(1))
                .setName(bookName);

        this.bookRepository.save(book);
    }

    @Test
    public void bookWithAuthor() {

        var author = this.authorRepository.findAll().get(3);

        var book = this.bookRepository.findAll().get(0);
        book.setName("Greatest of them all");
        book.setAuthor(author);
        this.bookRepository.saveAndFlush(book);
    }

    @Test
    public void addAuthorFromDTO() {
        var dto = new AuthorDTO();
        dto.setName("J.R.R Tolkien");

        var author = this.authorMapper.toEntity(dto);
        author = this.authorRepository.saveAndFlush(author);
        var result = this.authorMapper.toDTO(author);

        Assert.assertEquals(dto.getName(), result.getName());
    }

    @Test
    public void addBookToAuthor() {
        var authorId = 1L;
        var bookDTO = new BookDTO();
        bookDTO.setName("The Lord of the Rings");

        var author = this.authorRepository.getOne(authorId);
        var book = this.bookMapper.toEntity(bookDTO);

        book.setAuthor(author);
        book = this.bookRepository.saveAndFlush(book);

        Assert.assertEquals(book.getAuthor().getAuthorId(), authorId);
    }
}
