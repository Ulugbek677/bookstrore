package com.bookstore.exceptionshandler;

import com.bookstore.customexseptions.EmailAlreadyRegisteredException;
import com.bookstore.customexseptions.NoResourceFoundException;
import com.bookstore.customexseptions.UsernameAlreadyRegisteredException;
import com.bookstore.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFoundHandler(NoResourceFoundException ex){
        ex.printStackTrace();
        return ErrorDto.builder().errors(Map.of("error ",List.of(ex.getMessage()))).build();
    }
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDto emailAlreadyRegisteredHandler(EmailAlreadyRegisteredException ex){
        ex.printStackTrace();
        return ErrorDto.builder().errors(Map.of("email ",List.of(ex.getMessage()))).build();
    }

    @ExceptionHandler(UsernameAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorDto usernameAlreadyRegisteredHandler(UsernameAlreadyRegisteredException ex){
        ex.printStackTrace();
        return ErrorDto.builder().errors(Map.of("username ",List.of(ex.getMessage()))).build();
    }
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto notFoundHandler(Throwable ex){
        ex.printStackTrace();
        return ErrorDto.builder().errors(Map.of("error ",List.of(ex.getMessage()))).build();
    }
}
