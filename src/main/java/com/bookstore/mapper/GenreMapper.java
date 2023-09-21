package com.bookstore.mapper;

import com.bookstore.dto.GenreDTO;
import com.bookstore.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public Genre toEntity(GenreDTO genreDTO){
        return genreDTO == null ? null : new Genre(
                genreDTO.getId(),
                genreDTO.getName()
        );
    }

    public GenreDTO toDTO (Genre genre){
        return genre == null ? null : new GenreDTO(
                null,
                genre.getName()
        );
    }
}
