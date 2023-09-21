package com.bookstore.repository;

import com.bookstore.model.Genre;
import com.bookstore.service.GenreService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
