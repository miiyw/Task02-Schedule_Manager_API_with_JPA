package com.example.task2.dto;

import com.example.task2.config.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String userName;
    private String email;
    private String password;
    private UserRole role;
}
