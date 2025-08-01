package com.example.task2.repository;

import com.example.task2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이메일로 유저 조회 (Optional 반환)
    Optional<UserEntity> findByEmail(String email);

    // 중복 이메일 방지
    boolean existsByEmail(String email);
}
