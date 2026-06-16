import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { getCategories } from '../../api/categoryApi';
import axiosInstance from '../../api/axiosInstance';
import './PostWritePage.css';

function PostWritePage() {
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const [form, setForm] = useState({
        title: '',
        content: '',
        categoryId: '',
        subCategoryId: '',
    });
    const [subCategories, setSubCategories] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    // 카테고리 목록 불러오기
    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const data = await getCategories();
                setCategories(data);
            } catch (err) {
                console.error(err);
            }
        };
        fetchCategories();
    }, []);

    // 카테고리 선택 시 소분류 업데이트
    const handleCategoryChange = (e) => {
        const categoryId = e.target.value;
        const selected = categories.find(c => c.categoryId === parseInt(categoryId));
        setSubCategories(selected?.subCategories || []);
        setForm({ ...form, categoryId, subCategoryId: '' });
    };

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        if (!form.categoryId) {
            setError('카테고리를 선택해주세요');
            return;
        }

        setLoading(true);
        try {
            await axiosInstance.post('/api/posts', {
                title: form.title,
                content: form.content,
                categoryId: parseInt(form.categoryId),
                subCategoryId: form.subCategoryId ? parseInt(form.subCategoryId) : null,
            });
            navigate('/');
        } catch (err) {
            setError(err.response?.data?.message || '게시글 작성에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="write-container">
            <Navbar />
            <div className="write-content">
                <h2 className="write-title">글쓰기</h2>

                <form onSubmit={handleSubmit} className="write-form">

                    {/* 카테고리 선택 */}
                    <div className="form-row">
                        <select
                            name="categoryId"
                            value={form.categoryId}
                            onChange={handleCategoryChange}
                            className="write-select">
                            <option value="">카테고리 선택</option>
                            {categories.map(c => (
                                <option key={c.categoryId} value={c.categoryId}>
                                    {c.name}
                                </option>
                            ))}
                        </select>

                        {/* 소분류 선택 (중고거래일 때만 표시) */}
                        {subCategories.length > 0 && (
                            <select
                                name="subCategoryId"
                                value={form.subCategoryId}
                                onChange={handleChange}
                                className="write-select">
                                <option value="">소분류 선택</option>
                                {subCategories.map(s => (
                                    <option key={s.subCategoryId} value={s.subCategoryId}>
                                        {s.subCategoryName}
                                    </option>
                                ))}
                            </select>
                        )}
                    </div>

                    {/* 제목 */}
                    <input
                        type="text"
                        name="title"
                        value={form.title}
                        onChange={handleChange}
                        placeholder="제목을 입력하세요"
                        className="write-input"
                        required
                    />

                    {/* 내용 */}
                    <textarea
                        name="content"
                        value={form.content}
                        onChange={handleChange}
                        placeholder="내용을 입력하세요"
                        className="write-textarea"
                        required
                    />

                    {error && <p className="error-message">{error}</p>}

                    <div className="write-buttons">
                        <button type="button" className="cancel-btn"
                            onClick={() => navigate('/')}>
                            취소
                        </button>
                        <button type="submit" className="submit-btn" disabled={loading}>
                            {loading ? '등록 중...' : '등록'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default PostWritePage;