package com.bookstore.service.impl;

import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.dto.OrderDTO;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.Account;
import com.bookstore.repository.OrderRepository;
import com.bookstore.response.ApiResponse;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    @Override
    public ResponseEntity<OrderDTO> getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new NoResourceFoundException("Order Not Found"));
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @Override
    public ResponseEntity<ApiResponse> addOrder(OrderDTO orderDTO) {

        Order order = orderMapper.toEntity(orderDTO);
        order.setOrderDate(LocalDate.now());
        orderRepository.save(order);
        return  ResponseEntity.status(201).body(ApiResponse.builder().message("Order Created").build());
    }

    @Override
    public ResponseEntity<ApiResponse> deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return ResponseEntity.status(201).body(ApiResponse.builder().message("Order Deleted").build());
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders==null){
            throw new NoResourceFoundException("Order Not Found");
        }
        return ResponseEntity.ok(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Order> findOrderByUserIdAndBookId(Long userId, Long bookId) {
        Order currentOrder = findOrderByUserIdAndBookIdAndIsEndIsNull(userId, bookId);
        if (currentOrder == null) {
            return ResponseEntity.notFound().build();
        }


        currentOrder.setIsEnd(LocalDate.now());
        orderRepository.save(currentOrder);
        return ResponseEntity.ok(currentOrder);

    }
    public Order findOrderByUserIdAndBookIdAndIsEndIsNull(Long userId, Long bookId) {
        Account user = new Account();
        user.setId(userId);

        Book book = new Book();
        book.setId(bookId);

        return orderRepository.findByUserAndBookAndIsEndIsNull(user, book);
    }






}
