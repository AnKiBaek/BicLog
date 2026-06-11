import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { signUp } from '../../api/authApi';
import './Auth.css';

function SignUpPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ email: '', password: '', nickname: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (form.password.length < 8) {
            setError('비밀번호는 8자 이상이어야 합니다');
            return;
        }

        setLoading(true);
        try {
            await signUp(form.email, form.password, form.nickname);
            alert('회원가입이 완료됐습니다! 로그인해주세요.');
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || '회원가입에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-box">
                <h1 className="auth-logo">BicLog</h1>
                <p className="auth-subtitle">자전거 라이딩 기록 플랫폼</p>

                <form onSubmit={handleSubmit} className="auth-form">
                    <div className="form-group">
                        <label>이메일</label>
                        <input
                            type="email"
                            name="email"
                            value={form.email}
                            onChange={handleChange}
                            placeholder="이메일을 입력하세요"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>닉네임</label>
                        <input
                            type="text"
                            name="nickname"
                            value={form.nickname}
                            onChange={handleChange}
                            placeholder="닉네임을 입력하세요 (2자 이상)"
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>비밀번호</label>
                        <input
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            placeholder="비밀번호를 입력하세요 (8자 이상)"
                            required
                        />
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? '가입 중...' : '회원가입'}
                    </button>
                </form>

                <p className="auth-link">
                    이미 계정이 있으신가요? <Link to="/login">로그인</Link>
                </p>
            </div>
        </div>
    );
}

export default SignUpPage;