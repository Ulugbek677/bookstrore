package com.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private AccountDTO accountDTO;
    private BookDTO bookDTO;
    private LocalDate orderDate;
    private LocalDate isEnd;
}
