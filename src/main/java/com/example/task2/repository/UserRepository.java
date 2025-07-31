package com.example.task2.repository;

import com.example.task2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이메일로 유저 조회 (선택 사항)
    UserEntity findByEmail(String email);
}
