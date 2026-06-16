package com.biclog.backend.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequestDto {

    // 게시글 작성
    @Getter
    @NoArgsConstructor
    public static class Create {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "내용을 입력해주세요")
        private String content;

        @NotNull(message = "카테고리를 선택해주세요")
        private Long categoryId;

        private Long subCategoryId; // 선택사항 (자유게시판은 소분류 없음)

        private Long recordId; // 선택사항 (라이딩 기록 연동)
    }

    // 게시글 수정
    @Getter
    @NoArgsConstructor
    public static class Update {

        @NotBlank(message = "제목을 입력해주세요")
        private String title;

        @NotBlank(message = "내용을 입력해주세요")
        private String content;

        @NotNull(message = "카테고리를 선택해주세요")
        private Long categoryId;

        private Long subCategoryId; // 선택사항
    }
}