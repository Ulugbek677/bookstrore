package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long id);

    Order findByUserAndBookAndIsEndIsNull(Account user, Book book);
}
