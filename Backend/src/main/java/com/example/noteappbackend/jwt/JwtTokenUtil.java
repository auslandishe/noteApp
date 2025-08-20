package com.example.noteappbackend.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${jwttoken.secret}")
    private String secretKey;
    @Value("${jwttoken.expiration}")
    private long jwtTokenExpiration;


    public String generateJwtToken(String userName, List<String> roles) {
        return Jwts.builder()
                .subject(userName)
                .claim("roles", roles) // ⚠️ 關鍵：將角色列表作為一個 Claim 添加到 Payload 中
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtTokenExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateJwtToken(String token) {
        logger.info("Enter validateJwtToken");
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(UnsupportedJwtException exp) {
            logger.error("JWT claimsJws argument does not represent Claims JWS " + exp.getMessage());
        }catch(MalformedJwtException exp) {
            logger.error("JWT claimsJws string is not a valid JWS " + exp.getMessage());
        }catch(ExpiredJwtException exp) {
            logger.error("JWT Claims has an expiration time before the method is invoked " + exp.getMessage());
        }catch(IllegalArgumentException exp) {
            logger.error("JWT claimsJws string is null or empty or only whitespace " + exp.getMessage());
        }
        logger.info("Exit validateJwtToken");
        return false;
    }

    public String getUserNameFromJwtToken(String token) throws JwtException {
        logger.info("In getUserNameFromJwtToken");
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();

    }
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
