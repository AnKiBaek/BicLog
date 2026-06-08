package com.biclog.backend.domain.user.repository;

import com.biclog.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 회원 조회 (로그인, 중복 체크)
    Optional<User> findByEmail(String email);

    // 닉네임으로 회원 조회 (중복 체크)
    Optional<User> findByNickname(String nickname);

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 존재 여부 확인
    boolean existsByNickname(String nickname);
}