package com.example.task2.service;

import com.example.task2.dto.ScheduleRequestDto;
import com.example.task2.dto.ScheduleResponseDto;
import com.example.task2.entity.ScheduleEntity;
import com.example.task2.entity.ScheduleUserEntity;
import com.example.task2.entity.UserEntity;
import com.example.task2.repository.ScheduleRepository;
import com.example.task2.repository.ScheduleUserRepository;
import com.example.task2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service                            // Spring이 이 클래스를 서비스 계층의 Bean으로 등록
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final WeatherService weatherService;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, ScheduleUserRepository scheduleUserRepository, WeatherService weatherService) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.scheduleUserRepository = scheduleUserRepository;
        this.weatherService = weatherService;
    }

    /**
     * 일정 생성
     * @param request 일정 생성 요청 DTO
     * @param userId 일정 작성 유저의 고유 ID
     * @return 생성된 일정 정보 (응답 DTO)
     */
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto request, Long userId) {
        // 유저 존재 여부 확인
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        // 새 일정 생성
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setUser(user);  // 유저 고유 식별자 설정
        scheduleEntity.setTitle(request.getTitle());
        scheduleEntity.setContent(request.getContent());

        // 날씨 설정
        String todayWeather = weatherService.getTodayWeather();
        scheduleEntity.setWeather(todayWeather);

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
        return scheduleEntity.map(ScheduleResponseDto::fromEntityWithAssignedUsers);
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

        // 유저 정보 수정 (유저 고유 식별자로 유저를 수정)
        if (request.getUserId() != null) {
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
            scheduleEntity.setUser(user);
        }

        // 제목, 내용 수정
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            scheduleEntity.setTitle(request.getTitle());
        }
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            scheduleEntity.setContent(request.getContent());
        }

        // 수정된 일정 저장
        scheduleEntity = scheduleRepository.save(scheduleEntity);

        // 응답 DTO로 변환하여 반환
        return ScheduleResponseDto.fromEntity(scheduleEntity);
    }


    /**
     * 페이지네이션을 이용한 일정 조회
     * @param pageNo 페이지 번호
     * @param pageSize 페이지 크기
     * @return 페이지네이션된 일정 목록
     */
    public Page<ScheduleResponseDto> getSchedules(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize > 0 ? pageSize : 10, Sort.by(Sort.Order.desc("updatedAt")));

        Page<ScheduleEntity> page = scheduleRepository.findAll(pageable);

        List<ScheduleResponseDto> schedules = page.getContent().stream()
                .map(ScheduleResponseDto::fromEntity)  // 담당 유저 제외 변환 메서드 호출
                .collect(Collectors.toList());

        return new PageImpl<>(schedules, pageable, page.getTotalElements());
    }

    /**
     * 일정 삭제
     * @param id 삭제할 일정 ID
     */
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다.");
        }
        scheduleRepository.deleteById(id);
    }

    /**
     * 일정에 담당 유저 추가
     * @param scheduleId 일정 ID
     * @param userId 담당 유저 ID
     */
    public ScheduleUserEntity addUserToSchedule(Long scheduleId, Long userId) {
        // 일정 조회
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "일정이 존재하지 않습니다."));

        // 유저 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."));

        // 중간 테이블에 담당 유저 추가
        ScheduleUserEntity scheduleUser = new ScheduleUserEntity();
        scheduleUser.setSchedule(schedule);
        scheduleUser.setUser(user);

        return scheduleUserRepository.save(scheduleUser);
    }
}
