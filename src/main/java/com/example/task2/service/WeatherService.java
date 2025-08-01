package com.example.task2.service;

import com.example.task2.dto.WeatherResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTodayWeather() {
        String url = "https://f-api.github.io/f-api/weather.json";

        WeatherResponseDto[] responses = restTemplate.getForObject(url, WeatherResponseDto[].class);
        if (responses == null) return "날씨 정보 없음";

        // 오늘 날짜 MM-DD 형식으로 변환
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));

        for (WeatherResponseDto dto : responses) {
            if (today.equals(dto.getDate())) {
                return dto.getWeather();
            }
        }

        return "알 수 없음";
    }
}
