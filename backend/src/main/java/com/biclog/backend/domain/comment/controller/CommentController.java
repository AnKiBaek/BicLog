package com.biclog.backend.domain.comment.controller;

import com.biclog.backend.domain.comment.dto.CommentRequestDto;
import com.biclog.backend.domain.comment.dto.CommentResponseDto;
import com.biclog.backend.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 POST /api/posts/{postId}/comments
    @PostMapping
    public ResponseEntity<CommentResponseDto.Info> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto.Create request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(userId, postId, request));
    }

    // 댓글 목록 조회 GET /api/posts/{postId}/comments
    @GetMapping
    public ResponseEntity<List<CommentResponseDto.Info>> getComments(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId) {
        Long currentUserId = userDetails != null ? Long.parseLong(userDetails.getUsername()) : null;
        return ResponseEntity.ok(commentService.getComments(postId, currentUserId));
    }

    // 댓글 수정 PATCH /api/posts/{postId}/comments/{commentId}
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto.Info> updateComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto.Update request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(commentService.updateComment(userId, commentId, request));
    }

    // 댓글 삭제 DELETE /api/posts/{postId}/comments/{commentId}
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}