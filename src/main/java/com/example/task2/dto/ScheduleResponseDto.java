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

    private Long id;                    // 일정 고유 ID
    private String userName;            // 작성 유저명
    private String title;               // 할 일 제목
    private String content;             // 할 일 내용
    private String weather;             // 날씨 정보
    private LocalDateTime createdAt;    // 작성일
    private LocalDateTime updatedAt;    // 수정일
    private int commentCount;           // 댓글 개수

    // 담당 유저 목록 (단건 조회시에만 포함)
    private List<AssignedUserDto> assignedUsers;

    // 담당 유저 포함 생성자 (단건 조회용)
    public ScheduleResponseDto(
            Long id,
            String userName,
            String title,
            String content,
            String weather,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int commentCount,
            List<AssignedUserDto> assignedUsers
    ) {
        this.id = id;
        this.userName = userName;
        this.title = title;
        this.content = content;
        this.weather = weather;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.assignedUsers = assignedUsers;
    }

    // 담당 유저 제외 생성자 (전체 조회용)
    public ScheduleResponseDto(
            Long id,
            String userName,
            String title,
            String content,
            String weather,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            int commentCount
    ) {
        this(id, userName, title, content, weather, createdAt, updatedAt, commentCount, null);
    }

    // 단건 조회용 fromEntity (담당 유저 포함)
    public static ScheduleResponseDto fromEntityWithAssignedUsers(ScheduleEntity scheduleEntity) {
        List<AssignedUserDto> assignedUsers = scheduleEntity.getScheduleUsers() != null ?
                scheduleEntity.getScheduleUsers().stream()
                        .map(su -> new AssignedUserDto(su.getUser().getId(), su.getUser().getUserName()))
                        .collect(Collectors.toList())
                : null;

        return new ScheduleResponseDto(
                scheduleEntity.getId(),
                scheduleEntity.getUser() != null ? scheduleEntity.getUser().getUserName() : null,
                scheduleEntity.getTitle(),
                scheduleEntity.getContent(),
                scheduleEntity.getWeather(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getComments() != null ? scheduleEntity.getComments().size() : 0,
                assignedUsers
        );
    }

    // 전체 조회용 fromEntity (담당 유저 제외)
    public static ScheduleResponseDto fromEntity(ScheduleEntity scheduleEntity) {
        return new ScheduleResponseDto(
                scheduleEntity.getId(),
                scheduleEntity.getUser() != null ? scheduleEntity.getUser().getUserName() : null,
                scheduleEntity.getTitle(),
                scheduleEntity.getContent(),
                scheduleEntity.getWeather(),
                scheduleEntity.getCreatedAt(),
                scheduleEntity.getUpdatedAt(),
                scheduleEntity.getComments() != null ? scheduleEntity.getComments().size() : 0
        );
    }

    @Getter
    @AllArgsConstructor
    public static class AssignedUserDto {
        private Long id;
        private String userName;
    }
}
