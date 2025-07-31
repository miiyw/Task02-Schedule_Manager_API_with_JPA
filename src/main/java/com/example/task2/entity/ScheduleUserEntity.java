package com.example.task2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 고유 ID

    // 일정을 담당하는 유저
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @JsonBackReference(value = "schedule-scheduleUser")
    private ScheduleEntity schedule;  // 일정 (양방향 관계에서 '상위' 엔티티)

    // 일정 담당 유저
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-scheduleUser")
    private UserEntity user;  // 담당 유저 (양방향 관계에서 '하위' 엔티티)
}
