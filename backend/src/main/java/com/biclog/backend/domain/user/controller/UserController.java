package com.biclog.backend.domain.user.controller;

import com.biclog.backend.domain.user.dto.UserRequestDto;
import com.biclog.backend.domain.user.dto.UserResponseDto;
import com.biclog.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입 POST /api/users/signup
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto.Info> signUp(
            @Valid @RequestBody UserRequestDto.SignUp request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signUp(request));
    }

    // 내 정보 조회 GET /api/users/me
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto.Info> getMyInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.getMyInfo(userId));
    }

    // 닉네임 수정 PATCH /api/users/me/nickname
    @PatchMapping("/me/nickname")
    public ResponseEntity<UserResponseDto.Info> updateNickname(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserRequestDto.UpdateNickname request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(userService.updateNickname(userId, request));
    }

    // 비밀번호 수정 PATCH /api/users/me/password
    @PatchMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserRequestDto.UpdatePassword request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.updatePassword(userId, request);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴 DELETE /api/users/me
    @DeleteMapping("/me")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String password) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.delete(userId, password);
        return ResponseEntity.noContent().build();
    }
}