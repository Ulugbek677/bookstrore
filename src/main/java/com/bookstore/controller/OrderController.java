package com.bookstore.controller;

import com.bookstore.dto.OrderDTO;
import com.bookstore.model.Order;
import com.bookstore.response.ApiResponse;
import com.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable Long id){
        return orderService.getById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addOrder(@RequestBody OrderDTO orderDTO){
        return orderService.addOrder(orderDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteOrder(@PathVariable Long id){
        return orderService.deleteOrder(id);
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrderByUserId(@PathVariable Long userId){
        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/user/{userId}/book/{bookId}")
    public ResponseEntity<Order> getCurrentOrder(@PathVariable Long userId, @PathVariable Long bookId) {
        return orderService.findOrderByUserIdAndBookId(userId, bookId);

    }
}




