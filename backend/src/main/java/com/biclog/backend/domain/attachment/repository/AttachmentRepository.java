package com.biclog.backend.domain.attachment.repository;

import com.biclog.backend.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // 특정 게시글의 첨부파일 목록
    List<Attachment> findByPostPostId(Long postId);

    // 특정 게시글의 첨부파일 개수
    long countByPostPostId(Long postId);

    // 특정 게시글의 첨부파일 전체 삭제
    void deleteByPostPostId(Long postId);
}