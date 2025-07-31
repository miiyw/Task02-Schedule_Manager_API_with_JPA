package com.example.task2.service;

import com.example.task2.entity.UserEntity;
import com.example.task2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
