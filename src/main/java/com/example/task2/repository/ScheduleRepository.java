package com.example.task2.repository;

import com.example.task2.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // 기본적인 CRUD는 JpaRepository가 처리
}
