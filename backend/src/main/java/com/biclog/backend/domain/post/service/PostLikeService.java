package com.biclog.backend.domain.post.service;

import com.biclog.backend.domain.post.entity.Post;
import com.biclog.backend.domain.post.entity.PostLike;
import com.biclog.backend.domain.post.repository.PostLikeRepository;
import com.biclog.backend.domain.post.repository.PostRepository;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 좋아요 토글 (있으면 삭제, 없으면 추가)
    @Transactional
    public boolean toggleLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        if (postLikeRepository.existsByPostPostIdAndUserUserId(postId, userId)) {
            // 이미 좋아요 → 취소
            PostLike like = postLikeRepository.findByPostPostIdAndUserUserId(postId, userId)
                    .orElseThrow();
            postLikeRepository.delete(like);
            return false;
        } else {
            // 좋아요 추가
            postLikeRepository.save(PostLike.builder().post(post).user(user).build());
            return true;
        }
    }

    // 좋아요 수 조회
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostPostId(postId);
    }

    // 내가 좋아요 했는지 여부
    public boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsByPostPostIdAndUserUserId(postId, userId);
    }
}