package com.biclog.backend.domain.attachment.entity;

import com.biclog.backend.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ATTACHMENTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @Column(name = "ORIGINAL_NAME", nullable = false, length = 255)
    private String originalName; // 사용자가 올린 원본 파일명

    @Column(name = "STORED_NAME", nullable = false, length = 255)
    private String storedName; // 서버/S3에 저장되는 UUID 파일명

    @Column(name = "FILE_PATH", nullable = false, length = 500)
    private String filePath; // 저장 경로 또는 URL

    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize; // 바이트 단위

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Attachment(Post post, String originalName, String storedName,
                      String filePath, Long fileSize) {
        this.post         = post;
        this.originalName = originalName;
        this.storedName   = storedName;
        this.filePath     = filePath;
        this.fileSize     = fileSize;
    }
}