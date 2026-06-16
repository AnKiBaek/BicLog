package com.biclog.backend.domain.post.service;

import com.biclog.backend.domain.category.entity.Category;
import com.biclog.backend.domain.category.entity.SubCategory;
import com.biclog.backend.domain.category.repository.CategoryRepository;
import com.biclog.backend.domain.category.repository.SubCategoryRepository;
import com.biclog.backend.domain.post.dto.PostRequestDto;
import com.biclog.backend.domain.post.dto.PostResponseDto;
import com.biclog.backend.domain.post.entity.Post;
import com.biclog.backend.domain.post.repository.PostRepository;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    // 게시글 작성
    @Transactional
    public PostResponseDto.Detail createPost(Long userId, PostRequestDto.Create request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다"));

        SubCategory subCategory = null;
        if (request.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소분류입니다"));
        }

        Post post = Post.builder()
                .user(user)
                .category(category)
                .subCategory(subCategory)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return PostResponseDto.Detail.from(postRepository.save(post));
    }

    // 게시글 목록 조회 (페이징)
    public Page<PostResponseDto.Summary> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponseDto.Summary::from);
    }

    // 카테고리별 게시글 목록 조회
    public Page<PostResponseDto.Summary> getPostsByCategory(Long categoryId, Pageable pageable) {
        return postRepository.findByCategoryCategoryId(categoryId, pageable)
                .map(PostResponseDto.Summary::from);
    }

    // 내가 작성한 게시글 목록 조회
    public Page<PostResponseDto.Summary> getMyPosts(Long userId, Pageable pageable) {
        return postRepository.findByUserUserId(userId, pageable)
                .map(PostResponseDto.Summary::from);
    }

    // 게시글 상세 조회
    @Transactional
    public PostResponseDto.Detail getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
        post.increaseViewCount(); // 조회수 증가
        return PostResponseDto.Detail.from(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto.Detail updatePost(Long userId, Long postId, PostRequestDto.Update request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다"));

        SubCategory subCategory = null;
        if (request.getSubCategoryId() != null) {
            subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 소분류입니다"));
        }

        post.update(request.getTitle(), request.getContent(), category, subCategory);
        return PostResponseDto.Detail.from(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다");
        }

        postRepository.delete(post);
    }
}