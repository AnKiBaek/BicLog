package com.biclog.backend.domain.comment.service;

import com.biclog.backend.domain.comment.dto.CommentRequestDto;
import com.biclog.backend.domain.comment.dto.CommentResponseDto;
import com.biclog.backend.domain.comment.entity.Comment;
import com.biclog.backend.domain.comment.repository.CommentRepository;
import com.biclog.backend.domain.post.entity.Post;
import com.biclog.backend.domain.post.repository.PostRepository;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 작성
    @Transactional
    public CommentResponseDto.Info createComment(Long userId, Long postId, CommentRequestDto.Create request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();

        return CommentResponseDto.Info.from(commentRepository.save(comment), userId);
    }

    // 댓글 목록 조회
    public List<CommentResponseDto.Info> getComments(Long postId, Long currentUserId) {
        return commentRepository.findByPostPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(comment -> CommentResponseDto.Info.from(comment, currentUserId))
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponseDto.Info updateComment(Long userId, Long commentId, CommentRequestDto.Update request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다");
        }

        comment.updateContent(request.getContent());
        return CommentResponseDto.Info.from(comment, userId);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다"));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다");
        }

        commentRepository.delete(comment);
    }
}