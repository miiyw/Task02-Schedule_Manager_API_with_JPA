package com.example.task2.controller;

import com.example.task2.dto.ScheduleRequestDto;
import com.example.task2.dto.ScheduleResponseDto;
import com.example.task2.entity.ScheduleUserEntity;
import com.example.task2.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                    // 해당 클래스가 REST API의 컨트롤러임을 명시 (응답을 JSON으로 처리)
@RequestMapping("/schedules")   // 이 컨트롤러의 모든 요청은 "/schedules"로
public class ScheduleController {

    // 서비스 계층 의존성 주입
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 일정 생성 API
     * POST /schedules
     * @param request 일정 생성 요청 DTO
     * @param userId 일정 작성 유저의 고유 ID
     * @return 생성된 일정 정보 (응답 DTO)
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto request, @RequestParam Long userId) {
        ScheduleResponseDto save = scheduleService.saveSchedule(request, userId);
        return ResponseEntity.ok(save);
    }

    /**
     * 일정 단건 조회 API
     * GET /schedules/{id}
     * @param id 조회할 일정 ID
     * @return 조회된 일정 정보 (응답 DTO)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 일정 수정 API
     * PUT /schedules/{id}
     * @param id 수정할 일정 ID
     * @param request 수정할 내용이 담긴 요청 DTO
     * @return 수정된 일정 정보 (응답 DTO)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto request) {
        ScheduleResponseDto updatedSchedule = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(updatedSchedule);
    }

    /**
     * 페이지네이션을 사용하여 일정 목록을 조회
     * @param pageNo 페이지 번호
     * @param pageSize 페이지 크기
     * @return 페이지네이션된 일정 목록
     */
    @GetMapping
    public Page<ScheduleResponseDto> getSchedules(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return scheduleService.getSchedules(pageNo, pageSize);
    }

    /**
     * 일정 삭제 API
     * @param id 삭제할 일정 ID
     * @return HTTP 204 No Content 응답
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 일정에 담당 유저 추가
     * POST /schedules/{scheduleId}/users
     * @param scheduleId 일정 ID
     * @param userId 담당 유저 ID
     * @return 중간 테이블에 저장된 담당 유저 정보
     */
    @PostMapping("/{scheduleId}/users")
    public ResponseEntity<ScheduleUserEntity> addUserToSchedule(@PathVariable Long scheduleId, @RequestParam Long userId) {
        ScheduleUserEntity savedScheduleUser = scheduleService.addUserToSchedule(scheduleId, userId);
        return ResponseEntity.ok(savedScheduleUser);
    }
}
