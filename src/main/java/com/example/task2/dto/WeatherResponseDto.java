package com.example.task2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponseDto {
    private String date;        // 날짜
    private String weather;     // 날씨 정보
}
