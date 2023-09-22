package com.bookstore.service;

import com.bookstore.dto.UserRegistrationDTO;
import com.bookstore.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface AccountService {
    ResponseEntity<UserRegistrationDTO> accountRegister(UserRegistrationDTO userRegistrationDTO);

    ResponseEntity<UserRegistrationDTO> accountLogin(UserRegistrationDTO userRegistrationDTO);

}
