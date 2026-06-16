import axiosInstance from './axiosInstance';

// 게시글 목록 조회
export const getPosts = async (page = 0) => {
    const response = await axiosInstance.get(`/api/posts?page=${page}`);
    return response.data;
};

// 카테고리별 게시글 목록
export const getPostsByCategory = async (categoryId, page = 0) => {
    const response = await axiosInstance.get(`/api/posts/category/${categoryId}?page=${page}`);
    return response.data;
};

// 내가 작성한 게시글 목록
export const getMyPosts = async (page = 0) => {
    const response = await axiosInstance.get(`/api/posts/me?page=${page}`);
    return response.data;
};

// 게시글 상세
export const getPost = async (postId) => {
    const response = await axiosInstance.get(`/api/posts/${postId}`);
    return response.data;
};

// 게시글 작성
export const createPost = async (data) => {
    const response = await axiosInstance.post('/api/posts', data);
    return response.data;
};

// 게시글 수정
export const updatePost = async (postId, data) => {
    const response = await axiosInstance.patch(`/api/posts/${postId}`, data);
    return response.data;
};

// 게시글 삭제
export const deletePost = async (postId) => {
    await axiosInstance.delete(`/api/posts/${postId}`);
};

// 댓글 목록
export const getComments = async (postId) => {
    const response = await axiosInstance.get(`/api/posts/${postId}/comments`);
    return response.data;
};

// 댓글 작성
export const createComment = async (postId, content) => {
    const response = await axiosInstance.post(`/api/posts/${postId}/comments`, { content });
    return response.data;
};

// 댓글 삭제
export const deleteComment = async (postId, commentId) => {
    await axiosInstance.delete(`/api/posts/${postId}/comments/${commentId}`);
};

// 좋아요 토글
export const toggleLike = async (postId) => {
    const response = await axiosInstance.post(`/api/posts/${postId}/likes`);
    return response.data;
};

// 좋아요 정보
export const getLikeInfo = async (postId) => {
    const response = await axiosInstance.get(`/api/posts/${postId}/likes`);
    return response.data;
};