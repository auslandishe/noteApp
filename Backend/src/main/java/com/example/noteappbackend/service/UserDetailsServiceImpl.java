package com.example.noteappbackend.service;



import com.example.noteappbackend.entity.User;
import com.example.noteappbackend.repository.UserRepository;
import com.example.noteappbackend.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("UserDetailsService: loadUserByUsername - 嘗試加載用戶: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->{
                    logger.error("UserDetailsService: loadUserByUsername - 用戶 '{}' 未找到。", username);
                    return new UsernameNotFoundException("User with "
                            + "user name "+ username + " not found");
                });
        logger.info("UserDetailsService: loadUserByUsername - 成功加載用戶: {}", user.getUsername());
        return CustomUserDetails.createUserDetails(user);
    }
}
