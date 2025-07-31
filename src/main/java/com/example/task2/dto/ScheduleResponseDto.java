package com.example.task2.dto;

import com.example.task2.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ScheduleResponseDto {

    private Long id;                  // 일정 고유 ID
    private String userName;          // 작성 유저명
    private String title;             // 할 일 제목
    private String content;           // 할 일 내용
    private LocalDateTime createdAt;  // 작성일
    private LocalDateTime updatedAt;  // 수정일
    private int commentCount;         // 댓글 개수

    private List<AssignedUserDto> assignedUsers;  // 담당 유저 목록


    public ScheduleResponseDto(
            Long id,
            String userName,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int commentCount,
            List<AssignedUserDto> assignedUsers
    ) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.assignedUsers = assignedUsers;
    }

    // Entity를 ResponseDto로 변환하는 메서드
    public static ScheduleResponseDto fromEntity(ScheduleEntity scheduleEntity) {
        return new ScheduleResponseDto(
                scheduleEntity.getId(),
                scheduleEntity.getUser() != null ? scheduleEntity.getUser().getUserName() : null,
                scheduleEntity.getTitle(),
                scheduleEntity.getContent(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getComments() != null ? scheduleEntity.getComments().size() : 0,
                scheduleEntity.getScheduleUsers() != null ?
                        scheduleEntity.getScheduleUsers().stream()
                                .map(su -> new AssignedUserDto(su.getUser().getId(), su.getUser().getUserName()))
                                .collect(Collectors.toList()) : null
        );
    }

    @Getter
    @AllArgsConstructor
    public static class AssignedUserDto {
        private Long id;
        private String userName;
    }
}
