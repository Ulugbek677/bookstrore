package com.bookstore.service;

import com.bookstore.dto.OrderDTO;
import com.bookstore.model.Order;
import com.bookstore.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    ResponseEntity<OrderDTO> getById(Long id);

    ResponseEntity<ApiResponse> addOrder(OrderDTO orderDTO);

    ResponseEntity<ApiResponse> deleteOrder(Long id);

    ResponseEntity<List<OrderDTO>> getOrdersByUserId(Long userId);


    ResponseEntity<Order> findOrderByUserIdAndBookId(Long userId, Long bookId);
}
