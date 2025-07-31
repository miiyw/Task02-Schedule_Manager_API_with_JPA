package com.example.task2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 댓글 저장 및 수정 요청을 처리하는 DTO
public class CommentRequestDto {
    private String content;     // 댓글 내용
    private String userName;    // 작성 유저명
}
