import axiosInstance from './axiosInstance';

// 회원가입
export const signUp = async (email, password, nickname) => {
    const response = await axiosInstance.post('/api/users/signup', {
        email,
        password,
        nickname,
    });
    return response.data;
};

// 로그인
export const login = async (email, password) => {
    const response = await axiosInstance.post('/api/auth/login', {
        email,
        password,
    });
    return response.data;
};

// 내 정보 조회
export const getMyInfo = async () => {
    const response = await axiosInstance.get('/api/users/me');
    return response.data;
};

// 로그아웃 (토큰 삭제)
export const logout = () => {
    localStorage.removeItem('accessToken');
    window.location.href = '/login';
};