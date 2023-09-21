package com.bookstore.service;

import com.bookstore.dto.UserRegistrationDTO;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    ResponseEntity<UserRegistrationDTO> accountRegister(UserRegistrationDTO userRegistrationDTO);

    ResponseEntity<UserRegistrationDTO> accountLogin(UserRegistrationDTO userRegistrationDTO);
}
