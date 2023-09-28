package com.bookstore.service.impl;
import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.customexseptions.TokenValidationException;
import com.bookstore.customexseptions.UsernameAlreadyRegisteredException;
import com.bookstore.dto.UserRegistrationDTO;
import com.bookstore.mapper.UserMapper;
import com.bookstore.model.Account;
import com.bookstore.model.Role;
import com.bookstore.repository.AccountRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.service.AccountService;
import com.bookstore.service.JWTGeneratorService;
import com.bookstore.service.mail.MailService;
import com.bookstore.utility.JWTUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final JWTUtility jwtUtility;
    private final MailService mailService;
    private final JWTGeneratorService jwtGeneratorService;



//    @Override
//    public ResponseEntity<UserRegistrationDTO> accountRegister(UserRegistrationDTO userRegistrationDTO) {
//
//
//
//        accountRepository.findByUsername(userRegistrationDTO.getUsername())
//                .ifPresent(value -> {
//                    throw new UsernameAlreadyRegisteredException("Username has already been taken");
//                });
//
//
//        accountRepository.findByEmail(userRegistrationDTO.getEmail())
//                .ifPresent(value->{
//                    throw new UsernameAlreadyRegisteredException("has already been taken");
//                });
//
//        userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
//
//        Account account = accountRepository.save(userMapper.toEntity(userRegistrationDTO));
//
//
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                account.getEmail(), null , getUserRoles(account)
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//
//
//        return ResponseEntity.ok(userMapper.toDto(account));
//
//    }




    @Override
    public ResponseEntity<UserRegistrationDTO> accountRegister(UserRegistrationDTO userRegistrationDTO) {

        accountRepository.findByUsername(userRegistrationDTO.getUsername())
                .ifPresent(value -> {
                    throw new UsernameAlreadyRegisteredException("Username has already been taken");
                });

        accountRepository.findByEmail(userRegistrationDTO.getEmail())
                .ifPresent(value->{
                    throw new UsernameAlreadyRegisteredException("has already been taken");
                });
        userRegistrationDTO.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));

        Account account = accountRepository.save(userMapper.toEntity(userRegistrationDTO));

        Authentication auth = new UsernamePasswordAuthenticationToken(
                account.getEmail(), null , getUserRoles(account)
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtGeneratorService.jwtGenerate();
        sendTokenToUserEmail(userRegistrationDTO.getEmail(), token);


        return ResponseEntity.ok(userMapper.toDto(account));
    }




    // Send email link with token
    private void sendTokenToUserEmail(String email, String token) {
        String activationUrl = "http://localhost:8085/accounts/confirm";
        String subject = "Account Activation";
        String text = "To activate your account, please click on the following link:\n"
                + activationUrl + "?token=" + token;

        mailService.sendSimpleMessage(email, subject, text);
    }



    @Override
    public ResponseEntity<UserRegistrationDTO> accountLogin(UserRegistrationDTO userRegistrationDTO) {

        Account account = accountRepository
                .findByEmail(userRegistrationDTO.getEmail())
                .orElseThrow(()->new NoResourceFoundException("Not found"));

        if (!passwordEncoder.matches(userRegistrationDTO.getPassword(),account.getPassword())){
            throw new NoResourceFoundException("not authenticated");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                account.getEmail(), null, getUserRoles(account)
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return ResponseEntity.ok(userMapper.toDto(account));
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(()->new NoResourceFoundException("Account not found"));
        return User.builder().build();

    }

    public List<SimpleGrantedAuthority> getUserRoles(Account account){

        Role role = roleRepository.findById(account.getRole().getId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        List<Role> childRolesAndOwnRole = new ArrayList<>(roleRepository.findRoleByParentRole(role));
        childRolesAndOwnRole.add(role);

        List<SimpleGrantedAuthority> allRoles = childRolesAndOwnRole.stream()
                .map(roleItem -> new SimpleGrantedAuthority(roleItem.getName()))
                .collect(Collectors.toList());
        return allRoles;
    }


    //TODO check token

    public String confirmToken(String token) {
        try {
            if (jwtUtility.validate(token)) {
                String username = jwtUtility.getUsername(token);
                Collection<? extends GrantedAuthority> authorities = jwtUtility.getAuthorities(token);
                Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } else {
                throw new TokenValidationException("Token not valid");
            }
        } catch (Exception ex) {
            log.info("Token validation exception {}", ex.getMessage());



        }
        return "confirmed";
    }





}
