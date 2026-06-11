package com.biclog.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    // 회원가입
    @Getter
    @NoArgsConstructor
    public static class SignUp {

        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
        private String password;

        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하이어야 합니다")
        private String nickname;
    }

    // 로그인
    @Getter
    @NoArgsConstructor
    public static class Login {

        @Email(message = "이메일 형식이 올바르지 않습니다")
        @NotBlank(message = "이메일을 입력해주세요")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요")
        private String password;
    }

    // 닉네임 수정
    @Getter
    @NoArgsConstructor
    public static class UpdateNickname {

        @NotBlank(message = "닉네임을 입력해주세요")
        @Size(min = 2, max = 50, message = "닉네임은 2자 이상 50자 이하이어야 합니다")
        private String nickname;
    }

    // 비밀번호 수정
    @Getter
    @NoArgsConstructor
    public static class UpdatePassword {

        @NotBlank(message = "현재 비밀번호를 입력해주세요")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호를 입력해주세요")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
        private String newPassword;
    }
}