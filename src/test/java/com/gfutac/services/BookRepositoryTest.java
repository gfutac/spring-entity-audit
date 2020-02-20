package com.gfutac.services;

import com.gfutac.config.AbstractIntegrationTest;
import com.gfutac.repositories.AuthorRepository;
import com.gfutac.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BookRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void getBooksByJohnDoe() {
        var authorName = "John Doe";
        var books = this.bookRepository.getAllByAuthor_Name(authorName);
        Assert.assertNotEquals(0, books.size());
    }
}
