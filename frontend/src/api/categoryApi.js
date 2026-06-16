import axiosInstance from './axiosInstance';

// 카테고리 목록 조회
export const getCategories = async () => {
    const response = await axiosInstance.get('/api/categories');
    return response.data;
};