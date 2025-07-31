package com.example.task2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 일정 고유 ID

    //private String userName;    // 작성 유저명
    private String title;       // 할 일 제목
    private String content;     // 할 일 내용

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 작성일

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>(); // 일정과 댓글의 1:N 관계

    // 유저 고유 식별자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;  // 일정 작성자 (유저 고유 식별자)

    // 일정에 배치된 담당 유저들
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "schedule-scheduleUser")
    private List<ScheduleUserEntity> scheduleUsers;  // N:M 관계 중간 테이블 역할

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
