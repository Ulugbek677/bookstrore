package com.bookstore.service.impl;

import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.dto.OrderDTO;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.Account;
import com.bookstore.repository.AccountRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.response.ApiResponse;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AccountRepository accountRepository;
    @Override
    public ResponseEntity<OrderDTO> getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new NoResourceFoundException("Order Not Found"));
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @Override
    public ResponseEntity<ApiResponse> addOrder(OrderDTO orderDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findByEmail(auth.getName()).orElseThrow(()->new NoResourceFoundException("Account not found!"));

        Long CountActiveOrders = orderRepository.countUnfinishedOrdersByAccountId(account.getId());

        if (CountActiveOrders >= 5){
           throw new RuntimeException("Number of unread books more than 5.");
        }
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
        List<Order> orders = orderRepository.findByAccountId(userId);
        if (orders==null){
            throw new NoResourceFoundException("Order Not Found");
        }
        return ResponseEntity.ok(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Order> findOrderByUserIdAndOrderId(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = accountRepository.findByEmail(auth.getName()).orElseThrow(()-> new NoResourceFoundException("Account not found"));
        Order currentOrder = orderRepository.findByIdAndAccountIdAndIsEndIsNull(id, account.getId()).orElseThrow(()->new NoResourceFoundException("Order not found"));

        currentOrder.setIsEnd(LocalDate.now());
        orderRepository.save(currentOrder);
        return ResponseEntity.ok(currentOrder);

    }







}
