package com.bookstore.service;

import com.bookstore.dto.GenreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GenreService {
    ResponseEntity<GenreDTO> getGenreById(Long id);

    ResponseEntity<GenreDTO> addGenre(GenreDTO genreDTO);

    ResponseEntity<GenreDTO> updateGenre(GenreDTO genreDTO, Long id);

    void deleteGenre(Long id);

    ResponseEntity<List<GenreDTO>> getAll();
}
