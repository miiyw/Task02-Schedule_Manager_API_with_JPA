package com.example.task2.dto;

import java.time.LocalDateTime;

import com.example.task2.entity.CommentEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    private Long id;            // 댓글 고유 ID
    private String content;     // 댓글 내용
    private String userName;    // 작성 유저명
    private LocalDateTime createdAt;  // 작성일
    private LocalDateTime updatedAt;  // 수정일

    public CommentResponseDto(Long id, String content, String userName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.content = content;
        this.userName = userName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Entity를 ResponseDto로 변환하는 메서드
    public static CommentResponseDto fromEntity(CommentEntity commentEntity) {
        return new CommentResponseDto(
                commentEntity.getId(),
                commentEntity.getContent(),
                commentEntity.getUserName(),
                commentEntity.getCreatedAt(),
                commentEntity.getUpdatedAt()
        );
    }
}
