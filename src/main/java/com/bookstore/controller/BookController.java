package com.bookstore.controller;

import com.bookstore.dto.BookDTO;
import com.bookstore.response.ApiResponse;
import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id){
        return bookService.getById(id);
    }

    @GetMapping()
    public ResponseEntity<List<BookDTO>> getAll(){
        return bookService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addBook(@RequestBody BookDTO bookDTO) {
        return bookService.addBook(bookDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateBook(@RequestBody BookDTO bookDTO, @PathVariable Long id){
        return bookService.updateBook(bookDTO, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id){

        return bookService.deleteBook(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadBook(@PathVariable Long id) {
        return bookService.downloadBook(id);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {

       return bookService.uploadFile(file);
    }






}
