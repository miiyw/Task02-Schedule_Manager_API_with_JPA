package com.example.task2.service;

import com.example.task2.config.UserRole;
import com.example.task2.entity.UserEntity;
import com.example.task2.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 유저 저장
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    // 유저 단건 조회
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // 유저 전체 조회
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // 유저 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 회원 가입
    public String signUp(String userName, String email, String rawPassword, UserRole role) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);  // BCrypt 방식으로 암호화

        // 유저 저장
        UserEntity user = new UserEntity();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(role);

        userRepository.save(user);

        // JWT 발급
        return jwtUtil.createToken(user);
    }

    // 사용자 인증
    public UserEntity authenticateUser(String email, String password) {
        // 이메일로 사용자 조회 (Optional 반환)
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        // Optional이 비어있지 않으면 (값이 있으면)
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();  // 값을 안전하게 가져옴

            // 비밀번호 검증
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;  // 비밀번호가 일치하면 유저 반환
            }
        }
        return null;  // 이메일 또는 비밀번호가 일치하지 않으면 null 반환
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
