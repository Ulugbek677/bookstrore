package com.bookstore.mapper;

import com.bookstore.dto.AccountDTO;
import com.bookstore.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public Account toEntity(AccountDTO userDTO){
        return userDTO == null ? null : new Account(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getIsActive(),
                null
        );
    }

    public AccountDTO toDTO(Account user){
        return user == null ? null : new AccountDTO(
                null,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getIsActive(),
                user.getRole().getName()
        );
    }


}
