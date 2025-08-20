package com.example.noteappbackend.exception;

public class RoleNotFoundException extends  RuntimeException{
    public RoleNotFoundException(String message) {
        super(message);
    }
}
