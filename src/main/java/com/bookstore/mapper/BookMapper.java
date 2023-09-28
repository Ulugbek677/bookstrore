package com.bookstore.mapper;

import com.bookstore.dto.BookDTO;
import com.bookstore.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreMapper genreMapper;

    public Book toEntity(BookDTO bookDTO){
        return bookDTO == null ? null :new Book(
                null,
                bookDTO.getTitle(),
                bookDTO.getAuthor(),
                genreMapper.toEntity(bookDTO.getGenreDTO()),
                bookDTO.getDescription(),
                bookDTO.getFileName()
        );
    }
    public BookDTO toDto(Book book){
        return book == null ? null : new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                genreMapper.toDTO(book.getGenre()),
                book.getDescription(),
                book.getFileName()
        );
    }
}
