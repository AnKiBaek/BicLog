package com.biclog.backend.domain.post.controller;

import com.biclog.backend.domain.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 토글 POST /api/posts/{postId}/likes
    @PostMapping
    public ResponseEntity<Map<String, Object>> toggleLike(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        boolean liked = postLikeService.toggleLike(userId, postId);
        long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok(Map.of("liked", liked, "likeCount", count));
    }

    // 좋아요 수 조회 GET /api/posts/{postId}/likes
    @GetMapping
    public ResponseEntity<Map<String, Object>> getLikeInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId) {
        Long userId = userDetails != null ? Long.parseLong(userDetails.getUsername()) : null;
        long count = postLikeService.getLikeCount(postId);
        boolean liked = userId != null && postLikeService.isLiked(userId, postId);
        return ResponseEntity.ok(Map.of("liked", liked, "likeCount", count));
    }
}