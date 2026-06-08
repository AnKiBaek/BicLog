package com.biclog.backend.domain.post.repository;

import com.biclog.backend.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // 특정 게시글 + 유저의 좋아요 조회 (좋아요 여부 확인)
    Optional<PostLike> findByPostPostIdAndUserUserId(Long postId, Long userId);

    // 좋아요 존재 여부 확인
    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);

    // 특정 게시글의 좋아요 수
    long countByPostPostId(Long postId);
}