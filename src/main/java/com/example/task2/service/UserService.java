package com.example.task2.service;

import com.example.task2.config.PasswordEncoder;
import com.example.task2.entity.UserEntity;
import com.example.task2.repository.UserRepository;
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
    public String signUp(String userName, String email, String rawPassword) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 유저 저장
        UserEntity user = new UserEntity();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // JWT 발급
        return jwtUtil.generateToken(user);
    }
}
