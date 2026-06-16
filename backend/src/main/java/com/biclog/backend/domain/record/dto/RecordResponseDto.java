package com.biclog.backend.domain.record.dto;

import com.biclog.backend.domain.attachment.entity.Attachment;
import com.biclog.backend.domain.record.entity.Record;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class RecordResponseDto {

    // 라이딩 기록 목록 (그리드용 - 썸네일)
    @Getter
    @Builder
    public static class Summary {
        private Long recordId;
        private String title;
        private BigDecimal distanceKm;
        private Integer durationMin;
        private LocalDateTime startTime;
        private String thumbnailUrl; // 첫번째 사진 URL

        public static Summary from(Record record, String thumbnailUrl) {
            return Summary.builder()
                    .recordId(record.getRecordId())
                    .title(record.getTitle())
                    .distanceKm(record.getDistanceKm())
                    .durationMin(record.getDurationMin())
                    .startTime(record.getStartTime())
                    .thumbnailUrl(thumbnailUrl)
                    .build();
        }
    }

    // 라이딩 기록 상세
    @Getter
    @Builder
    public static class Detail {
        private Long recordId;
        private String title;
        private String content;
        private BigDecimal distanceKm;
        private Integer durationMin;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String gpxData;
        private LocalDateTime createdAt;
        private List<AttachmentInfo> attachments; // 사진 목록

        public static Detail from(Record record, List<AttachmentInfo> attachments) {
            return Detail.builder()
                    .recordId(record.getRecordId())
                    .title(record.getTitle())
                    .content(record.getContent())
                    .distanceKm(record.getDistanceKm())
                    .durationMin(record.getDurationMin())
                    .startTime(record.getStartTime())
                    .endTime(record.getEndTime())
                    .gpxData(record.getGpxData())
                    .createdAt(record.getCreatedAt())
                    .attachments(attachments)
                    .build();
        }
    }

    // 첨부파일 정보
    @Getter
    @Builder
    public static class AttachmentInfo {
        private Long fileId;
        private String originalName;
        private String filePath;

        public static AttachmentInfo from(Attachment attachment) {
            return AttachmentInfo.builder()
                    .fileId(attachment.getFileId())
                    .originalName(attachment.getOriginalName())
                    .filePath(attachment.getFilePath())
                    .build();
        }
    }
}