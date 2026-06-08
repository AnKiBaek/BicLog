package com.biclog.backend.domain.post.entity;

import com.biclog.backend.domain.category.entity.Category;
import com.biclog.backend.domain.record.entity.Record;
import com.biclog.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "POSTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECORD_ID") // nullable — 라이딩 기록 연동 선택사항
    private Record record;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content; // CLOB — 대용량 본문

    @Column(name = "VIEW_COUNT", nullable = false)
    private int viewCount = 0;

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Post(User user, Category category, Record record, String title, String content) {
        this.user     = user;
        this.category = category;
        this.record   = record;
        this.title    = title;
        this.content  = content;
    }

    // == 수정 메서드 ==
    public void update(String title, String content, Category category, Record record) {
        this.title    = title;
        this.content  = content;
        this.category = category;
        this.record   = record;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }
}