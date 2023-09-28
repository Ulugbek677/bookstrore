package com.bookstore.mapper;
import com.bookstore.dto.OrderDTO;
import com.bookstore.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final AccountMapper userMapper;
    private final BookMapper bookMapper;

    public Order toEntity(OrderDTO orderDTO){
        return orderDTO == null ? null : new Order(
                orderDTO.getId(),
                userMapper.toEntity(orderDTO.getAccountDTO()),
                bookMapper.toEntity(orderDTO.getBookDTO()),
                orderDTO.getOrderDate(),
                null
        );
    }

   public OrderDTO toDto(Order order){
        return order == null ? null : new OrderDTO(
                order.getId(),
                userMapper.toDTO(order.getAccount()),
                bookMapper.toDto(order.getBook()),
                order.getOrderDate(),
                null
        );
   }

}
