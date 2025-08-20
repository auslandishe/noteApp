package com.example.noteappbackend.jwt;

import com.example.noteappbackend.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter{
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenUtil jwtTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsService, HandlerExceptionResolver handlerExceptionResolver){
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("Entering doFilterInternal");
        String token = getTokenFromRequest(request);
        System.out.println("Token-- " + token);
        try {

            if (token != null && jwtTokenUtil.validateJwtToken(token)) {
                String username = jwtTokenUtil.getUserNameFromJwtToken(token);
                //System.out.println("User Name--JwtTokenFilter-- " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //System.out.println("Authorities--JwtTokenFilter-- " + userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }catch(JwtException ex) {
            // resolve exception so that it can be handled by GlobalExceptionHandler
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
        logger.info("Exiting doFilterInternal");
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("Token from Request-- " + token);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            // remove "Bearer "
            return token.substring(7, token.length());
        }
        return null;
    }
}