package com.example.task2.repository;

import com.example.task2.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    /**
     * 특정 일정(scheduleId)에 소속된 모든 댓글 목록을 조회
     * @param scheduleId 일정 ID
     * @return 해당 일정의 모든 댓글
     */
    List<CommentEntity> findByScheduleId(Long scheduleId);
}
