package com.example.noteappbackend.controller;

import com.example.noteappbackend.dto.AuthResponseDto;
import com.example.noteappbackend.dto.RegisterDto;
import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.service.AuthService;
import com.example.noteappbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins="http://localhost:4200")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthService authService;
    public AuthController(UserService userService, AuthService authService){
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Validated @RequestBody User user) {
        System.out.println("AuthController111 -- userLogin");
        AuthResponseDto authResponseDto = authService.userLogin(user);
        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping("/register")
    public ResponseEntity<String> userRegister(@Validated @RequestBody RegisterDto registerDto) {
        System.out.println("註冊開始!");
        try {
            if(userService.isUserExists(registerDto.getUsername())){
                return ResponseEntity.badRequest().body("用戶名已存在!");
            }
            authService.userRegister(registerDto);
            return ResponseEntity.ok("註冊成功");
        }catch(Exception e) {
            logger.error("註冊失敗 訊息如下: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register User");
        }

    }
}
