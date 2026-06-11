import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8081',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 요청할 때마다 JWT 토큰 자동으로 헤더에 붙여줌
axiosInstance.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('accessToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 응답 에러 처리
axiosInstance.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // 토큰 만료 시 로그인 페이지로 이동
            localStorage.removeItem('accessToken');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default axiosInstance;