package com.bookstore.repository;

import com.bookstore.dto.GenreDTO;
import com.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(name = "select * from books b join genres g on b.genre_id = g.id\n" +
            "where g.name = 'Drama'\n", nativeQuery = true)
    List<Book> getBookByAndGenre(GenreDTO genreDTO);
}
