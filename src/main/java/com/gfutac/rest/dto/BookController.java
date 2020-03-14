package com.gfutac.rest.dto;

import com.gfutac.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/book/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    @Autowired
    private BookService bookService;

    @PutMapping(value = "/book/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO updateBook(@PathVariable long bookId, @RequestBody BookDTO bookDTO) {
        return this.bookService.updateBook(bookId, bookDTO);
    }
}
