package com.example.noteappbackend.exception;

import com.example.noteappbackend.dto.ErrorResponseDto;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExcpetionHandler {
    @ExceptionHandler(value = AuthException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthException(AuthException ex) {
        //System.out.println("In global error");
        String message = "Error while processing request";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.CREATED, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UsernameNotFoundException ex) {
        //System.out.println("In global error - UsernameNotFoundException");
        String message = "User name not found";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // This is thrown from UsernamePasswordAuthenticationFilter when attempting Authentication
    // and credentials are wrong
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
        //System.out.println("In global error - handleAuthenticationException");
        String message = "User name or password is wrong";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    //When using @PreAuthorize in Spring Boot with Spring Security, if the specified authorization expression evaluates
    //to false, an AccessDeniedException is thrown
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
        String message = "Access Denied: You do not have permission to access this resource";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.FORBIDDEN, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<ErrorResponseDto> handleTokenException(Exception ex) {
        String message = "Token has a problem";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.UNAUTHORIZED, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        String message = "Error while processing request";
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getMessage());
        return new ResponseEntity<ErrorResponseDto>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
