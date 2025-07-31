package com.example.task2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    private String userName;    // 작성 유저명
    private String title;       // 할 일 제목
    private String content;     // 할 일 내용
}
