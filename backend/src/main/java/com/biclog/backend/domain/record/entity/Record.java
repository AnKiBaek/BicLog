package com.biclog.backend.domain.record.entity;

import com.biclog.backend.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RECORDS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "DURATION_MIN", nullable = false)
    private Integer durationMin;

    @Column(name = "DISTANCE_KM", nullable = false, precision = 5, scale = 2)
    private BigDecimal distanceKm;

    @Lob
    @Column(name = "GPX_DATA")
    private String gpxData; // CLOB — GPX XML/JSON 경로 데이터

    @CreationTimestamp
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Lob
    @Column(name = "CONTENT")
    private String content; // 라이딩 중 있었던 일

    @Builder
    public Record(User user, String title, String content, LocalDateTime startTime, LocalDateTime endTime,
                  Integer durationMin, BigDecimal distanceKm, String gpxData) {
        this.user        = user;
        this.title       = title;
        this.content     = content;
        this.startTime   = startTime;
        this.endTime     = endTime;
        this.durationMin = durationMin;
        this.distanceKm  = distanceKm;
        this.gpxData     = gpxData;
    }

    public void update(String title, String content, LocalDateTime startTime, LocalDateTime endTime,
                       Integer durationMin, BigDecimal distanceKm, String gpxData) {
        this.title       = title;
        this.content     = content;
        this.startTime   = startTime;
        this.endTime     = endTime;
        this.durationMin = durationMin;
        this.distanceKm  = distanceKm;
        this.gpxData     = gpxData;
    }
}