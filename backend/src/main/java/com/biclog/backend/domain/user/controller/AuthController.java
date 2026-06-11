package com.biclog.backend.domain.user.controller;

import com.biclog.backend.domain.user.dto.UserRequestDto;
import com.biclog.backend.domain.user.dto.UserResponseDto;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import com.biclog.backend.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 로그인 POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto.LoginResult> login(
            @Valid @RequestBody UserRequestDto.Login request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다");
        }

        String token = jwtTokenProvider.generateToken(user.getUserId());

        return ResponseEntity.ok(UserResponseDto.LoginResult.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .role(user.getRole().name())
                .build());
    }
}