package com.example.task2.controller;

import com.example.task2.dto.CommentRequestDto;
import com.example.task2.dto.CommentResponseDto;
import com.example.task2.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                    // 해당 클래스가 REST API의 컨트롤러임을 명시 (응답을 JSON으로 처리)
@RequestMapping("/comments")    // 이 컨트롤러의 모든 요청은 "/comments"로 시작
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 저장 API
     * @param scheduleId 댓글이 달릴 일정 ID
     * @param dto 댓글 내용, 작성자 정보
     * @return 생성된 댓글 엔티티
     */
    @PostMapping("/{scheduleId}")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long scheduleId,
                                                            @RequestBody CommentRequestDto dto) {
        CommentResponseDto savedComment = commentService.saveComment(scheduleId, dto);
        return ResponseEntity.ok(savedComment);
    }

    /**
     * 특정 일정(scheduleId)에 등록된 모든 댓글 조회
     * @param scheduleId 일정 ID
     * @return 댓글 리스트
     */
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByScheduleId(@PathVariable Long scheduleId) {
        List<CommentResponseDto> comments = commentService.getCommentsByScheduleId(scheduleId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 단건 조회
     * @param commentId 댓글 ID
     * @return 해당 댓글
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getCommentById(@PathVariable Long commentId) {
        return commentService.getCommentById(commentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 댓글 수정
     * @param commentId 수정할 댓글 ID
     * @param dto 수정할 내용
     * @return 수정된 댓글
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody CommentRequestDto dto) {
        CommentResponseDto updatedComment = commentService.updateComment(commentId, dto);
        return ResponseEntity.ok(updatedComment);
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     * @return 204 No Content
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
