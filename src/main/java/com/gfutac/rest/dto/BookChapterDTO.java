package com.gfutac.rest.dto;

import lombok.Data;

@Data
public class BookChapterDTO {
    private String name;
    private BookDTO book;
}
