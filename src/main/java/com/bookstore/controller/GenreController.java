package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.dto.GenreDTO;
import com.bookstore.service.BookService;
import com.bookstore.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;
    private final BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id){
        return genreService.getGenreById(id);
    }

    @PostMapping
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO){
        return genreService.addGenre(genreDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@RequestBody GenreDTO genreDTO, @PathVariable Long id){
        return genreService.updateGenre(genreDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id){
        genreService.deleteGenre(id);

    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getALl(){
        return genreService.getAll();
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> getGenreAllBook(@RequestBody GenreDTO genreDTO){
        return bookService.getGenreAllBook(genreDTO);
    }





}

