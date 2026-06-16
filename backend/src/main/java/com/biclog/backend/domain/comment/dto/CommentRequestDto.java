package com.biclog.backend.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentRequestDto {

    @Getter
    @NoArgsConstructor
    public static class Create {
        @NotBlank(message = "댓글 내용을 입력해주세요")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class Update {
        @NotBlank(message = "댓글 내용을 입력해주세요")
        private String content;
    }
}