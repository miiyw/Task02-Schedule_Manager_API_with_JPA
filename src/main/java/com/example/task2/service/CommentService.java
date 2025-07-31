package com.example.task2.service;

import com.example.task2.dto.CommentRequestDto;
import com.example.task2.dto.CommentResponseDto;
import com.example.task2.entity.CommentEntity;
import com.example.task2.entity.ScheduleEntity;
import com.example.task2.repository.CommentRepository;
import com.example.task2.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    // JPA를 통한 DB 접근을 위한 의존성 주입
    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 댓글 생성
     * @param scheduleId 연결할 일정 ID
     * @param request 댓글 내용 및 작성자 정보
     * @return 저장된 댓글 엔티티
     */
    public CommentResponseDto saveComment(Long scheduleId, CommentRequestDto request) {
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("일정이 존재하지 않습니다."));

        CommentEntity comment = new CommentEntity();
        comment.setContent(request.getContent());
        comment.setUserName(request.getUserName());
        comment.setSchedule(schedule);  // 일정과 연관 설정

        comment = commentRepository.save(comment);

        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 특정 일정(scheduleId)에 달린 모든 댓글 조회
     * @param scheduleId 일정 ID
     * @return 댓글 목록
     */
    public List<CommentResponseDto> getCommentsByScheduleId(Long scheduleId) {
        List<CommentEntity> comments = commentRepository.findByScheduleId(scheduleId);
        return comments.stream()
                .map(CommentResponseDto::fromEntity)
                .toList();
    }

    /**
     * 단일 댓글 조회
     * @param commentId 댓글 ID
     * @return 조회된 댓글
     */
    public Optional<CommentResponseDto> getCommentById(Long commentId) {
        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        return comment.map(CommentResponseDto::fromEntity);
    }

    /**
     * 댓글 수정
     * @param commentId 수정할 댓글 ID
     * @param request 수정할 내용
     * @return 수정된 댓글 엔티티
     */
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto request) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        comment.setContent(request.getContent());
        comment.setUserName(request.getUserName());
        comment = commentRepository.save(comment);

        return CommentResponseDto.fromEntity(comment);
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     */
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new IllegalArgumentException("댓글이 존재하지 않습니다.");
        }

        commentRepository.deleteById(commentId);
    }
}
