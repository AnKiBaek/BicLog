package com.biclog.backend.domain.post.repository;

import com.biclog.backend.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 카테고리별 게시글 목록 (페이징)
    Page<Post> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    // 특정 유저의 게시글 목록 (페이징)
    Page<Post> findByUserUserId(Long userId, Pageable pageable);

    // 제목 검색 (페이징)
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}