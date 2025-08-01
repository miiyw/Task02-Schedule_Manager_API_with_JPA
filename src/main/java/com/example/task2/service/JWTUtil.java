package com.example.task2.service;

import com.example.task2.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    // SecretKey를 직접 하드코딩하지 않고, 안전하게 생성하는 방법
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 최소 256비트 길이의 비밀 키 생성

    // JWT 발급
    public String generateToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())  // 사용자 이메일을 Subject로 설정
                .setIssuedAt(new Date())      // 발급 시각 설정
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 만료
                .signWith(SECRET_KEY)         // 자동으로 적절한 비밀 키를 사용하여 서명
                .compact();                   // JWT 생성
    }
}
