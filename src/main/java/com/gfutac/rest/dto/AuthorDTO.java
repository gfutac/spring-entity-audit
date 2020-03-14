package com.gfutac.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthorDTO {
    @NotEmpty
    private String name;
}
