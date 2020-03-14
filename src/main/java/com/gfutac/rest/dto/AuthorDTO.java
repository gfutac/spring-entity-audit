package com.gfutac.rest.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class AuthorDTO {
    @NotEmpty
    private String name;

    @JsonManagedReference
    private List<BookDTO> books;
}
