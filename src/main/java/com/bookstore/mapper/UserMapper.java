package com.bookstore.mapper;

import com.bookstore.dto.UserRegistrationDTO;
import com.bookstore.model.Account;
import com.bookstore.model.Role;
import com.bookstore.service.JWTGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final JWTGeneratorService jwtGeneratorService;
    private static final Role DEFAULT_USER_ROLE = new Role(2L, "USER", null);

    public Account toEntity(UserRegistrationDTO userRegister){
        return new Account(
                userRegister.getId(),
                userRegister.getUsername(),
                userRegister.getPassword(),
                userRegister.getEmail(),
                true,
                DEFAULT_USER_ROLE

        );
    }

    public UserRegistrationDTO toDto(Account account){
        return new UserRegistrationDTO(
                account.getId(),
                account.getUsername(),
                account.getPassword(),
                account.getEmail(),
                jwtGeneratorService.jwtGenerate()


        );
    }
}
