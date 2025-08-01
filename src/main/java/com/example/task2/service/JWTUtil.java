package com.example.task2.service;

import com.example.task2.entity.UserEntity;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    // SecretKey를 직접 하드코딩하지 않고, 안전하게 생성하는 방법
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 최소 256비트 길이의 비밀 키 생성
    // 24시간 (1일)
    private static final long TOKEN_EXPIRY = 1000 * 60 * 60 * 24;

    // JWT 발급
//    public String generateToken(UserEntity user) {
//        return Jwts.builder()
//                .setSubject(user.getEmail())  // 사용자 이메일을 Subject로 설정
//                .setIssuedAt(new Date())      // 발급 시각 설정
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 만료
//                .signWith(SECRET_KEY)         // 자동으로 적절한 비밀 키를 사용하여 서명
//                .compact();                   // JWT 생성
//    }

    // JWT 토큰 검증 (Claims를 반환)
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 유효성 검증(예외 처리 포함)
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 토큰입니다.");
        } catch (MalformedJwtException e) {
            System.out.println("토큰 형식이 잘못되었습니다.");
        } catch (Exception e) {
            System.out.println("토큰 검증 실패: " + e.getMessage());
        }
        return false;
    }

    public String createToken(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .claim("role", user.getRole().name())  // 권한 포함
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
