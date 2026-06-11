package com.biclog.backend.domain.user.dto;

import com.biclog.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserResponseDto {

    // 회원가입 / 마이페이지 응답
    @Getter
    @Builder
    public static class Info {
        private Long userId;
        private String email;
        private String nickname;
        private String role;
        private LocalDateTime createdAt;

        public static Info from(User user) {
            return Info.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .role(user.getRole().name())
                    .createdAt(user.getCreatedAt())
                    .build();
        }
    }

    // 로그인 응답 (JWT 토큰 포함)
    @Getter
    @Builder
    public static class LoginResult {
        private String accessToken;
        private String tokenType; // "Bearer"
        private Long userId;
        private String nickname;
        private String role;
    }
}