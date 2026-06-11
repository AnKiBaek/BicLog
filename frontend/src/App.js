import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/auth/LoginPage.js';
import SignUpPage from './pages/auth/SignUpPage';
import MainPage from './pages/MainPage';
 
function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignUpPage />} />
                <Route path="*" element={<Navigate to="/login" />} />
            </Routes>

          
        </BrowserRouter>
    );
}
 
export default App;
 