package com.example.task2.repository;

import com.example.task2.entity.ScheduleUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleUserRepository extends JpaRepository<ScheduleUserEntity, Long> {
    // 필요한 추가 쿼리 메서드가 있으면 여기에 추가
}
