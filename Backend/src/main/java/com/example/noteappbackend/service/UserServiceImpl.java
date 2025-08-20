package com.example.noteappbackend.service;

import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public boolean isUserExists(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}