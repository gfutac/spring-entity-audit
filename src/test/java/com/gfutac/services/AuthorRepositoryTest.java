package com.gfutac.services;

import com.gfutac.config.AbstractIntegrationTest;
import com.gfutac.entities.Author;
import com.gfutac.repositories.AuthorRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AuthorRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void getAllUsers() {
        var authors = this.authorRepository.findAll();
        Assert.assertNotEquals(0, authors.size());
    }

    @Test
    public void canAddAuthor() {
        var authorName = "JRR Tolkien";

        var author = new Author()
                .setAuthorId(100)
                .setName(authorName);

        this.authorRepository.save(author);

        Assert.assertTrue(this.authorRepository.existsByName(authorName));
    }
}
