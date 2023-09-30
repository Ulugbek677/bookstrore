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
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;
    private static final String PATH_PDF = "/Users/ulugbek/booksPdf";

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

    @Override
    public ResponseEntity<byte[]> downloadBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()-> new NoResourceFoundException("Book not found"));

        // TODO Retrieve the file from the server
        String fileName = book.getFileName();
        Path filePath = Paths.get(PATH_PDF).resolve(fileName);

        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileContent.length)
                    .body(fileContent);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        Path uploadPath = Paths.get(PATH_PDF);

        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "File uploaded successfully : " + fileName;
        } catch (IOException e) {

            return "Error occurred while uploading the file.\n : " + e.getMessage();
        }
    }


}
