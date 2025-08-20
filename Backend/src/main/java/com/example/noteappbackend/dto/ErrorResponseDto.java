package com.example.noteappbackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponseDto {
    private HttpStatus statusCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String exceptionMessage;
    ErrorResponseDto() {}
    public ErrorResponseDto(HttpStatus statusCode, String message, String exceptionMessage) {
        this.statusCode = statusCode;
        this.message = message;
        this.exceptionMessage = exceptionMessage;
        this.timestamp = LocalDateTime.now();
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
    public void setStatus(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getExceptionMessage() {
        return exceptionMessage;
    }
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
