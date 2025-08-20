package com.example.noteappbackend.service;

import com.example.noteappbackend.Enum.Roles;
import com.example.noteappbackend.dto.AuthResponseDto;
import com.example.noteappbackend.dto.RegisterDto;
import com.example.noteappbackend.entity.Role;
import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.exception.RoleNotFoundException;
import com.example.noteappbackend.jwt.JwtTokenUtil;
import com.example.noteappbackend.repository.RoleRepository;
import com.example.noteappbackend.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AuthServiceImpl implements AuthService{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    public AuthServiceImpl(RoleRepository roleRepository, UserService userService,
                           PasswordEncoder encoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil){
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public void userRegister(RegisterDto registerDto) {
        logger.info("Entering userSignup");
        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setUsername(registerDto.getUsername());
        user.setPassword(encoder.encode(registerDto.getPassword()));
        //System.out.println("Encoded password--- " + user.getPassword());
        String[] roleArr = registerDto.getRoles();

        // give User role by default
        if(roleArr == null) {
            roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
        }
        //
        for(String role: roleArr) {
            switch(role.toLowerCase()) {
                case "admin":
                    roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
                    break;
                case "user":
                    roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
                    break;
                default:
                    throw new RoleNotFoundException("Specified role not found");
            }
        }
        user.setRole(roles);
        userService.save(user);
        logger.info("Exiting userSignup");
    }

    @Override
    public AuthResponseDto userLogin(User user) {
        logger.info("AuthServiceImpl: userLogin - 嘗試為用戶 '{}' 進行認證。", user.getUsername());
        logger.debug("AuthServiceImpl: userLogin - 傳入的用戶名: {}, 密碼長度: {}", user.getUsername(), user.getPassword() != null ? user.getPassword().length() : 0); // 不要直接日誌密碼

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            logger.info("AuthServiceImpl: userLogin - 用戶 '{}' 認證成功。", user.getUsername());
        } catch (Exception e) {
            logger.error("AuthServiceImpl: userLogin - 用戶 '{}' 認證失敗: {}", user.getUsername(), e.getMessage(), e);
            // 這裡可以重新拋出異常或返回特定的錯誤響應
            throw e; // 重新拋出，讓 Spring Security 的異常處理器來處理
        }


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        logger.info("AuthServiceImpl: userLogin - 用戶 '{}' 的角色: {}", userDetails.getUsername(), roles);

        // ⚠️ 關鍵修改：在生成 JWT 時傳入角色列表
        String token = jwtTokenUtil.generateJwtToken(userDetails.getUsername(), roles);
        logger.info("AuthServiceImpl: userLogin - 為用戶 '{}' 生成 JWT。", userDetails.getUsername());
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setToken(token);
        authResponseDto.setRoles(roles);
        logger.info("AuthServiceImpl: userLogin - 返回 AuthResponseDto。");
        return authResponseDto;

    }
}
