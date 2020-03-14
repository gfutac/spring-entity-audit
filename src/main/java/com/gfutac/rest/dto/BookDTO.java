package com.gfutac.rest.dto;

import lombok.Data;

@Data
public class BookDTO {
    private String name;
    private AuthorDTO author;
}
