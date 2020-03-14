package com.gfutac.rest.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
public class BookDTO {
    private String name;

    @JsonBackReference
    private AuthorDTO author;
}
