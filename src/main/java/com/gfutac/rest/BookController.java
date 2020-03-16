package com.gfutac.rest;

import com.gfutac.model.Book;
import com.gfutac.rest.dto.BookChapterDTO;
import com.gfutac.rest.dto.BookDTO;
import com.gfutac.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/book/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController extends BaseAuditController<Book> {

    @Autowired
    private BookService bookService;

    @PutMapping(value = "/book/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO updateBook(@PathVariable long bookId, @RequestBody BookDTO bookDTO) {
        return this.bookService.updateBook(bookId, bookDTO);
    }

    @PutMapping(value = "/book/{bookId}/add-chapter")
    @ResponseStatus(HttpStatus.CREATED)
    public BookChapterDTO addChapter(@PathVariable long bookId, @RequestBody BookChapterDTO bookChapterDTO) {
        return this.bookService.addChapterToBook(bookId, bookChapterDTO);
    }

}
