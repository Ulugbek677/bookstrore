package com.bookstore.service;

import com.bookstore.dto.BookDTO;
import com.bookstore.dto.GenreDTO;
import com.bookstore.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {

    ResponseEntity<BookDTO> getById(Long id);
    ResponseEntity<List<BookDTO>> getAll();

    ResponseEntity<ApiResponse> addBook(BookDTO bookDTO);
    ResponseEntity<ApiResponse> updateBook(BookDTO bookDTO, Long id);

    ResponseEntity<ApiResponse> deleteBook(Long id);

    ResponseEntity<List<BookDTO>> getGenreAllBook(GenreDTO genreDTO);


    ResponseEntity<byte[]> downloadBook(Long id);
}
