package com.bookstore.service;
import com.bookstore.utility.JWTUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JWTGeneratorService {
    private final JWTUtility jwtUtility;
    public String jwtGenerate(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return jwtUtility.generate(
                    authentication.getName(),
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(", "))
            );
        }
        return "";
    }



}
