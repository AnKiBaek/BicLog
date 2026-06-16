import { Link, useNavigate } from 'react-router-dom';
import { logout } from '../api/authApi';
import './Navbar.css';

function Navbar() {
    const navigate = useNavigate();
    const isLoggedIn = !!localStorage.getItem('accessToken');

    const handleLogout = () => {
        logout();
        navigate('/login');
    };

    return (
        <nav className="navbar">
            <div className="navbar-inner">
                <Link to="/" className="navbar-logo">BicLog</Link>
                <div className="navbar-menu">
                    <Link to="/?category=7" className="navbar-link">자유게시판</Link>
                    <Link to="/?category=6" className="navbar-link">중고거래</Link>
                </div>
                <div className="navbar-auth">
                    {isLoggedIn ? (
                        <>
                            <Link to="/mypage" className="navbar-link">마이페이지</Link>
                            <button onClick={handleLogout} className="navbar-logout">로그아웃</button>
                        </>
                    ) : (
                        <>
                            <Link to="/login" className="navbar-link">로그인</Link>
                            <Link to="/signup" className="navbar-btn">회원가입</Link>
                        </>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navbar;