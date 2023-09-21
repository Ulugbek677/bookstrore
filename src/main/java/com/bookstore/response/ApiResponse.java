package com.bookstore.response;

import lombok.*;

@Builder
@Data
public class ApiResponse {
    private Integer status;
    private String message;
    private Boolean isSuccess;
}
