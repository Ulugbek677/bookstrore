package com.bookstore.controller;

import com.bookstore.dto.UserRegistrationDTO;
import com.bookstore.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {


    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationDTO> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        return accountService.accountRegister(userRegistrationDTO);
    }



    @PostMapping("/login")
    public ResponseEntity<UserRegistrationDTO> loginUser(@RequestBody UserRegistrationDTO userRegistrationDTO){
        return accountService.accountLogin(userRegistrationDTO);
    }



}
