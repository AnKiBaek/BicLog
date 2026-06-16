package com.biclog.backend.domain.comment.dto;

import com.biclog.backend.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentResponseDto {

    @Getter
    @Builder
    public static class Info {
        private Long commentId;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private boolean isMyComment; // 내 댓글 여부

        public static Info from(Comment comment, Long currentUserId) {
            return Info.builder()
                    .commentId(comment.getCommentId())
                    .nickname(comment.getUser().getNickname())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .isMyComment(comment.getUser().getUserId().equals(currentUserId))
                    .build();
        }
    }
}