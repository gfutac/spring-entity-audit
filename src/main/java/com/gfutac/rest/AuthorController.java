package com.gfutac.rest;

import com.gfutac.rest.dto.AuthorDTO;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/author/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping(value = "/author")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO addAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        return this.authorService.addAuthor(authorDTO);
    }

    @GetMapping(value = "/author/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO getAuthor(@PathVariable long authorId) {
        return this.authorService.getAuthor(authorId);
    }

    @PutMapping(value = "/author/{authorId}/book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO addBookToAuthor(@PathVariable long authorId, @RequestBody BookDTO bookDTO) {
        return this.authorService.addBookToAuthor(authorId, bookDTO);
    }
}
