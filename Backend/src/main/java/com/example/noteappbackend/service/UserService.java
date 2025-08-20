package com.example.noteappbackend.service;

import com.example.noteappbackend.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    boolean isUserExists(String userName);
    User save(User user);
}
