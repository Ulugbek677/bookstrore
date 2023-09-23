package com.bookstore.controller;

import com.bookstore.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activate")
public class ActivationController {
    @Autowired
    private JWTUtility jwtUtility;

    @PostMapping
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        if (jwtUtility.validate(token)) {
            // Foydalanuvchi tasdiqlandi, u aktivlashtirilgan
            // Bu yerdan boshqa amallar bajara olishingiz mumkin, masalan, bazaga o'zgarishlar kiritish

            return ResponseEntity.ok("Account has been activated successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
}
