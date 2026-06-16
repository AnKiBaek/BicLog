import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import axiosInstance from '../../api/axiosInstance';
import './MainPage.css';

function MainPage() {
    const navigate = useNavigate();
    const [searchParams, setSearchParams] = useSearchParams();
    const [posts, setPosts] = useState([]);
    const [loading, setLoading] = useState(true);

    const categoryId = searchParams.get('category');

    useEffect(() => {
        const fetchPosts = async () => {
            setLoading(true);
            try {
                const url = categoryId
                    ? `/api/posts/category/${categoryId}`
                    : '/api/posts';
                const response = await axiosInstance.get(url);
                setPosts(response.data.content);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchPosts();
    }, [categoryId]);

    return (
        <div className="main-container">
            <Navbar />
            <div className="main-content">

                {/* 카테고리 탭 */}
                <div className="category-tabs">
                    <button
                        className={`tab ${!categoryId ? 'active' : ''}`}
                        onClick={() => setSearchParams({})}>
                        전체
                    </button>
                    <button
                        className={`tab ${categoryId === '7' ? 'active' : ''}`}
                        onClick={() => setSearchParams({ category: 7 })}>
                        자유게시판
                    </button>
                    <button
                        className={`tab ${categoryId === '6' ? 'active' : ''}`}
                        onClick={() => setSearchParams({ category: 6 })}>
                        중고거래
                    </button>
                </div>

                {/* 게시글 목록 */}
                <div className="post-list">
                    {loading ? (
                        <p className="post-empty">불러오는 중...</p>
                    ) : posts.length === 0 ? (
                        <p className="post-empty">게시글이 없습니다</p>
                    ) : (
                        posts.map(post => (
                            <div key={post.postId} className="post-item"
                                onClick={() => navigate(`/posts/${post.postId}`)}>
                                <div className="post-item-header">
                                    <span className="post-category">{post.categoryName}</span>
                                    {post.subCategoryName && (
                                        <span className="post-subcategory">{post.subCategoryName}</span>
                                    )}
                                </div>
                                <h3 className="post-title">{post.title}</h3>
                                <div className="post-item-footer">
                                    <span className="post-author">{post.nickname}</span>
                                    <span className="post-view">조회 {post.viewCount}</span>
                                    <span className="post-date">
                                        {new Date(post.createdAt).toLocaleDateString()}
                                    </span>
                                </div>
                            </div>
                        ))
                    )}
                </div>

                {/* 게시글 작성 버튼 */}
                {localStorage.getItem('accessToken') && (
                    <button className="write-btn" onClick={() => navigate('/posts/write')}>
                        글쓰기
                    </button>
                )}
            </div>
        </div>
    );
}

export default MainPage;