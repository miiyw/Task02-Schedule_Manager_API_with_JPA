package com.example.task2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String userName;
    private String email;
    private String password;
}
