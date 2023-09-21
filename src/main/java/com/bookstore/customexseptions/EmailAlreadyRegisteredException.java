package com.bookstore.customexseptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAlreadyRegisteredException extends RuntimeException {
    private String message;
}
