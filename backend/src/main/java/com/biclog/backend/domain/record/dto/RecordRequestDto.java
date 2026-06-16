package com.biclog.backend.domain.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecordRequestDto {

    // 라이딩 기록 작성
    @Getter
    @NoArgsConstructor
    public static class Create {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        private String content; // 라이딩 중 있었던 일 (선택)

        @NotNull(message = "시작 시간을 입력해주세요")
        private LocalDateTime startTime;

        @NotNull(message = "종료 시간을 입력해주세요")
        private LocalDateTime endTime;

        @NotNull(message = "운동 시간을 입력해주세요")
        private Integer durationMin;

        @NotNull(message = "거리를 입력해주세요")
        private BigDecimal distanceKm;

        private String gpxData; // GPX 데이터 (선택)
    }

    // 라이딩 기록 수정
    @Getter
    @NoArgsConstructor
    public static class Update {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        private String content;

        @NotNull(message = "시작 시간을 입력해주세요")
        private LocalDateTime startTime;

        @NotNull(message = "종료 시간을 입력해주세요")
        private LocalDateTime endTime;

        @NotNull(message = "운동 시간을 입력해주세요")
        private Integer durationMin;

        @NotNull(message = "거리를 입력해주세요")
        private BigDecimal distanceKm;

        private String gpxData;
    }
}