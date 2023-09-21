package com.bookstore.service.impl;

import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.dto.BookDTO;
import com.bookstore.dto.GenreDTO;
import com.bookstore.mapper.BookMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Genre;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.GenreRepository;
import com.bookstore.response.ApiResponse;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;
    @Override
    public ResponseEntity<BookDTO> getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()->new NoResourceFoundException());
        return ResponseEntity.ok(bookMapper.toDto(book));
    }

    @Override
    public ResponseEntity<List<BookDTO>> getAll() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books.stream().map(bookMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<ApiResponse> addBook(BookDTO bookDTO) {
        Genre genre = genreRepository.findByName(bookDTO.getGenreDTO().getName());
        if(genre == null){
            throw new NoResourceFoundException("Not Genre Found");
        }

        Book book = bookMapper.toEntity(bookDTO);
        book.setGenre(genre);
        bookRepository.save(book);

        ApiResponse response = ApiResponse.builder()
                .status(201)
                .message("Book Created")
                .isSuccess(true)
                .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponse> updateBook(BookDTO bookDTO, Long id) {
        Book book = bookMapper.toEntity(bookDTO);
        book.setId(id);
        bookRepository.save(book);
        return ResponseEntity.status(201).body(ApiResponse.builder().message("Book Edited").build());
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBook(Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.status(201).body(ApiResponse.builder().message("Book Deleted").build());
    }

    @Override
    public ResponseEntity<List<BookDTO>> getGenreAllBook(GenreDTO genreDTO) {
        List<Book> bookByAndGenre = bookRepository.getBookByAndGenre(genreDTO);
        return ResponseEntity.ok(bookByAndGenre
                .stream().map(bookMapper::toDto)
                .collect(Collectors.toList()));
    }
}
