import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/auth/LoginPage';
import SignUpPage from './pages/auth/SignUpPage';
import MainPage from './pages/main/MainPage';
import PostWritePage from './pages/post/PostWritePage';
import PostDetailPage from './pages/post/PostDetailPage';
import MyPage from './pages/mypage/MyPage';
import RecordWritePage from './pages/record/RecordWritePage';
import RecordDetailPage from './pages/record/RecordDetailPage';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignUpPage />} />
                <Route path="/posts/write" element={<PostWritePage />} />
                <Route path="/posts/:postId" element={<PostDetailPage />} />
                <Route path="/mypage" element={<MyPage />} />
                <Route path="/records/write" element={<RecordWritePage />} />
                <Route path="/records/:recordId" element={<RecordDetailPage />} />
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;