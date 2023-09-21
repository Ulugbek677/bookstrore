package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GenreDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    private String name;
}
