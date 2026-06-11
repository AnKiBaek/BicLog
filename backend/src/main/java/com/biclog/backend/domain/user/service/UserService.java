package com.biclog.backend.domain.user.service;

import com.biclog.backend.domain.user.dto.UserRequestDto;
import com.biclog.backend.domain.user.dto.UserResponseDto;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public UserResponseDto.Info signUp(UserRequestDto.SignUp request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .nickname(request.getNickname())
                .build();

        return UserResponseDto.Info.from(userRepository.save(user));
    }

    // 내 정보 조회 (마이페이지)
    public UserResponseDto.Info getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        return UserResponseDto.Info.from(user);
    }

    // 닉네임 수정
    @Transactional
    public UserResponseDto.Info updateNickname(Long userId, UserRequestDto.UpdateNickname request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다");
        }

        user.updateNickname(request.getNickname());
        return UserResponseDto.Info.from(user);
    }

    // 비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, UserRequestDto.UpdatePassword request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다");
        }

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    // 회원 탈퇴
    @Transactional
    public void delete(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        userRepository.delete(user);
    }
}