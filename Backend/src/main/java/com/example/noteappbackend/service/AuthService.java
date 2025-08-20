package com.example.noteappbackend.service;

import com.example.noteappbackend.dto.AuthResponseDto;
import com.example.noteappbackend.dto.RegisterDto;
import com.example.noteappbackend.entity.User;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

public interface AuthService {
    void userRegister(RegisterDto registerDto) throws RoleNotFoundException;
    AuthResponseDto userLogin(User user);
}
