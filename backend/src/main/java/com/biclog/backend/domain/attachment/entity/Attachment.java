package com.biclog.backend.domain.attachment.entity;

import com.biclog.backend.domain.post.entity.Post;
import com.biclog.backend.domain.record.entity.Record;
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
    @JoinColumn(name = "POST_ID")  // nullable (Post 또는 Record 둘 중 하나)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORD_ID")  // nullable
    private Record record;

    @Column(name = "ORIGINAL_NAME", nullable = false, length = 255)
    private String originalName;

    @Column(name = "STORED_NAME", nullable = false, length = 255)
    private String storedName;

    @Column(name = "FILE_PATH", nullable = false, length = 500)
    private String filePath;

    @Column(name = "FILE_SIZE", nullable = false)
    private Long fileSize;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Attachment(Post post, Record record, String originalName, String storedName,
                      String filePath, Long fileSize) {
        this.post         = post;
        this.record       = record;
        this.originalName = originalName;
        this.storedName   = storedName;
        this.filePath     = filePath;
        this.fileSize     = fileSize;
    }
}