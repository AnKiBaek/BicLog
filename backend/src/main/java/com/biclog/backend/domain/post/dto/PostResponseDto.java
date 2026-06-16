package com.biclog.backend.domain.post.dto;

import com.biclog.backend.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class PostResponseDto {

    // 게시글 목록 조회
    @Getter
    @Builder
    public static class Summary {
        private Long postId;
        private String title;
        private String nickname;       // 작성자 닉네임
        private String categoryName;   // 카테고리명
        private String subCategoryName; // 소분류명 (없을 수 있음)
        private int viewCount;
        private LocalDateTime createdAt;

        public static Summary from(Post post) {
            return Summary.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .nickname(post.getUser().getNickname())
                    .categoryName(post.getCategory().getName())
                    .subCategoryName(post.getSubCategory() != null ? post.getSubCategory().getSubCategoryName() : null)
                    .viewCount(post.getViewCount())
                    .createdAt(post.getCreatedAt())
                    .build();
        }
    }

    // 게시글 상세 조회
    @Getter
    @Builder
    public static class Detail {
        private Long postId;
        private String title;
        private String content;
        private String nickname;
        private String categoryName;
        private String subCategoryName;
        private int viewCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static Detail from(Post post) {
            return Detail.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .nickname(post.getUser().getNickname())
                    .categoryName(post.getCategory().getName())
                    .subCategoryName(post.getSubCategory() != null ? post.getSubCategory().getSubCategoryName() : null)
                    .viewCount(post.getViewCount())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }
    }
}