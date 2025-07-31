package com.example.task2.service;

import com.example.task2.dto.ScheduleRequestDto;
import com.example.task2.dto.ScheduleResponseDto;
import com.example.task2.entity.ScheduleEntity;
import com.example.task2.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service                            // Spring이 이 클래스를 서비스 계층의 Bean으로 등록
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // Spring의 의존성 주입(Dependency Injection)을 사용하여 ScheduleRepository 자동으로 주입
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        // 생성자 주입을 통해 scheduleRepository 필드에 전달된 ScheduleRepository 객체 할당
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 새로운 일정을 생성하여 DB에 저장
     * @param request 일정 생성 요청 DTO
     * @return 저장된 일정의 응답 DTO
     */
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto request) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setUserName(request.getUserName());
        scheduleEntity.setTitle(request.getTitle());
        scheduleEntity.setContent(request.getContent());

        // DB에 저장
        scheduleEntity = scheduleRepository.save(scheduleEntity);

        return ScheduleResponseDto.fromEntity(scheduleEntity);
    }

    /**
     * 특정 ID에 해당하는 일정 조회
     * @param id 일정 ID
     * @return 일정 응답 DTO
     */
    public Optional<ScheduleResponseDto> getScheduleById(Long id) {
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(id);
        return scheduleEntity.map(ScheduleResponseDto::fromEntity);
    }

    /**
     * 특정 ID에 해당하는 일정 수정
     * @param id 일정 ID
     * @param request 수정할 제목과 내용을 담은 DTO
     * @return 수정된 일정의 응답 DTO
     */
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto request) {
        ScheduleEntity scheduleEntity = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정이 존재하지 않습니다."));

        // 입력된 값이 null이 아닌 경우에만 수정
        if (request.getUserName() != null) {
            scheduleEntity.setUserName(request.getUserName()); // userName 수정
        }
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            scheduleEntity.setTitle(request.getTitle());       // title 수정
        }
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            scheduleEntity.setContent(request.getContent());   // content 수정
        }

        // 수정된 일정 저장
        scheduleEntity = scheduleRepository.save(scheduleEntity);

        // 응답 DTO로 변환하여 반환
        return ScheduleResponseDto.fromEntity(scheduleEntity);
    }
}
