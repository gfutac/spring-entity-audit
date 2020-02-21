package com.gfutac.services;

import com.gfutac.config.AbstractIntegrationTest;
import com.gfutac.entities.Author;
import com.gfutac.entities.Book;
import com.gfutac.repositories.AuthorRepository;
import com.gfutac.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class AuthorRepositoryTest extends AbstractIntegrationTest {

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

        var author = new Author()
                .setName(authorName);
        var afterSave = this.authorRepository.save(author);

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
        var authorId = 100L;
        var authorName = "JRR Tolkien";

        var book = new Book()
                .setName("LOTR");

        var author = new Author()
                .setName(authorName)
                .setBooks(Collections.singletonList(book));

        author = this.authorRepository.save(author);

        Assert.assertNotEquals(0, author.getBooks().size());
    }
}
