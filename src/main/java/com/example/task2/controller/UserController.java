package com.example.task2.controller;

import com.example.task2.dto.LoginRequestDto;
import com.example.task2.dto.SignupRequestDto;
import com.example.task2.entity.UserEntity;
import com.example.task2.service.JWTUtil;
import com.example.task2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // 유저 저장
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // 유저 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 유저 전체 조회
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody SignupRequestDto signupRequest) {
        try {
            String token = userService.signUp(
                    signupRequest.getUserName(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword(),
                    signupRequest.getRole()
            );

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("msg", "회원 가입 성공");
            response.put("statusCode", 200);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("statusCode", 400);
            errorResponse.put("msg", e.getMessage());

            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 이메일로 유저 먼저 조회
        Optional<UserEntity> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                    "msg", "존재하지 않는 이메일입니다.",
                    "statusCode", 401
            ));
        }

        UserEntity user = userOpt.get();

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of(
                    "msg", "비밀번호가 일치하지 않습니다.",
                    "statusCode", 401
            ));
        }

        // 성공 시
        //String token = jwtUtil.generateToken(user);
        String token = jwtUtil.createToken(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(Map.of(
                        "msg", "로그인 성공",
                        "statusCode", 200
                ));
    }
}
