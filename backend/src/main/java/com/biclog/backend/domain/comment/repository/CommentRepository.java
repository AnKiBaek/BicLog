package com.biclog.backend.domain.comment.repository;

import com.biclog.backend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 (작성순)
    List<Comment> findByPostPostIdOrderByCreatedAtAsc(Long postId);

    // 특정 게시글의 댓글 개수
    long countByPostPostId(Long postId);
}