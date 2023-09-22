package com.bookstore.service.impl;
import com.bookstore.config.SecurityConfig;
import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.customexseptions.UsernameAlreadyRegisteredException;
import com.bookstore.dto.AccountDTO;
import com.bookstore.dto.UserRegistrationDTO;
import com.bookstore.mapper.AccountMapper;
import com.bookstore.mapper.UserMapper;
import com.bookstore.model.Account;
import com.bookstore.model.Role;
import com.bookstore.repository.AccountRepository;
import com.bookstore.repository.RoleRepository;
import com.bookstore.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;



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



        return ResponseEntity.ok(userMapper.toDto(account));
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
                .orElseThrow(()->new NoResourceFoundException("account not found"));
        return new User(account.getUsername(), account.getPassword(), getUserRoles(account));
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


}
