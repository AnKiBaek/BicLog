import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../../api/authApi';
import './Auth.css';

function LoginPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ email: '', password: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            const data = await login(form.email, form.password);
            localStorage.setItem('accessToken', data.accessToken);
            navigate('/');
        } catch (err) {
            setError(err.response?.data?.message || '로그인에 실패했습니다');
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
                        <label>비밀번호</label>
                        <input
                            type="password"
                            name="password"
                            value={form.password}
                            onChange={handleChange}
                            placeholder="비밀번호를 입력하세요"
                            required
                        />
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <button type="submit" className="auth-button" disabled={loading}>
                        {loading ? '로그인 중...' : '로그인'}
                    </button>
                </form>

                <p className="auth-link">
                    계정이 없으신가요? <Link to="/signup">회원가입</Link>
                </p>
            </div>
        </div>
    );
}

export default LoginPage;