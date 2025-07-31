package com.example.task2.repository;

import com.example.task2.entity.ScheduleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    // 수정일을 기준으로 내림차순 정렬하여 페이지네이션 처리
    Page<ScheduleEntity> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
