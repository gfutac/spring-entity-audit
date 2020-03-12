package com.gfutac.services;

import com.gfutac.config.AbstractIntegrationTest;
import com.gfutac.model.Author;
import com.gfutac.model.Book;
import com.gfutac.repositories.AuthorRepository;
import com.gfutac.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class AuditTests extends AbstractIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

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
}
