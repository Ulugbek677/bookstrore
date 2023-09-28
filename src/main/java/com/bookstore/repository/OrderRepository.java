package com.bookstore.repository;

import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountId(Long id);

    @Query(value = "select * from orders where id = ?1 and account_id = ?2 and is_end IS NULL", nativeQuery = true)
    Optional<Order> findByIdAndAccountIdAndIsEndIsNull(Long id, Long accountId);


    @Query("SELECT COUNT(o) FROM Order o WHERE o.account.id = :accountId AND o.isEnd IS NULL")
    Long countUnfinishedOrdersByAccountId(Long accountId);

}
