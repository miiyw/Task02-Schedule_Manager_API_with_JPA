package com.example.task2.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            // 유저 고유 ID

    private String userName;    // 유저명
    private String email;       // 이메일

    private String password;    // 비밀번호

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 작성일

    @Column(nullable = false)
    private LocalDateTime updatedAt;  // 수정일

    // 일정에 할당된 유저들
    @OneToMany(mappedBy = "user")
    @JsonManagedReference(value = "user-scheduleUser")
    private List<ScheduleUserEntity> schedules;  // 일정과 담당 유저 관계

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
