import axiosInstance from './axiosInstance';

// 내 라이딩 기록 목록
export const getMyRecords = async (page = 0) => {
    const response = await axiosInstance.get(`/api/records/me?page=${page}&size=9`);
    return response.data;
};

// 라이딩 기록 상세
export const getRecord = async (recordId) => {
    const response = await axiosInstance.get(`/api/records/${recordId}`);
    return response.data;
};

// 라이딩 기록 작성 (multipart)
export const createRecord = async (data, files) => {
    const formData = new FormData();
    formData.append('request', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    if (files) {
        files.forEach(file => formData.append('files', file));
    }
    const response = await axiosInstance.post('/api/records', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    });
    return response.data;
};

// 라이딩 기록 삭제
export const deleteRecord = async (recordId) => {
    await axiosInstance.delete(`/api/records/${recordId}`);
};