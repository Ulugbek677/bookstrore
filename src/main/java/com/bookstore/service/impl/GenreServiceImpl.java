package com.bookstore.service.impl;

import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.dto.GenreDTO;

import com.bookstore.mapper.GenreMapper;
import com.bookstore.model.Genre;
import com.bookstore.repository.GenreRepository;
import com.bookstore.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    @Override
    public ResponseEntity<GenreDTO> getGenreById(Long id) {
        return ResponseEntity.ok(genreMapper.toDTO(genreRepository
                .findById(id)
                .orElseThrow(() -> new NoResourceFoundException("Genre not found"))));
    }

    @Override
    public ResponseEntity<GenreDTO> addGenre(GenreDTO genreDTO) {
        Genre save = genreRepository.save(genreMapper.toEntity(genreDTO));
        return ResponseEntity.ok(genreMapper.toDTO(save));
    }

    @Override
    public ResponseEntity<GenreDTO> updateGenre(GenreDTO genreDTO, Long id) {
        Genre genre = genreMapper.toEntity(genreDTO);
        genre.setId(id);
        return ResponseEntity.ok(genreMapper.toDTO( genreRepository.save(genre)));
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<List<GenreDTO>> getAll() {
        List<Genre> genreAll = genreRepository.findAll();
        return ResponseEntity.ok(genreAll
                .stream()
                .map(genre -> genreMapper.toDTO(genre))
                .collect(Collectors.toList()));
    }
}
