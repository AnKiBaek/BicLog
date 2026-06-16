package com.biclog.backend.domain.post.controller;

import com.biclog.backend.domain.post.dto.PostRequestDto;
import com.biclog.backend.domain.post.dto.PostResponseDto;
import com.biclog.backend.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 작성 POST /api/posts
    @PostMapping
    public ResponseEntity<PostResponseDto.Detail> createPost(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody PostRequestDto.Create request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId, request));
    }

    // 게시글 목록 조회 GET /api/posts
    @GetMapping
    public ResponseEntity<Page<PostResponseDto.Summary>> getPosts(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getPosts(pageable));
    }

    // 카테고리별 게시글 목록 조회 GET /api/posts/category/{categoryId}
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<PostResponseDto.Summary>> getPostsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.getPostsByCategory(categoryId, pageable));
    }

    // 내가 작성한 게시글 목록 GET /api/posts/me
    @GetMapping("/me")
    public ResponseEntity<Page<PostResponseDto.Summary>> getMyPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(postService.getMyPosts(userId, pageable));
    }

    // 게시글 상세 조회 GET /api/posts/{postId}
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto.Detail> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    // 게시글 수정 PATCH /api/posts/{postId}
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto.Detail> updatePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDto.Update request) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(postService.updatePost(userId, postId, request));
    }

    // 게시글 삭제 DELETE /api/posts/{postId}
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}