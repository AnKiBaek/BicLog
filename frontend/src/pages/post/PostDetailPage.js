import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { getPost, deletePost, getComments, createComment, deleteComment, toggleLike, getLikeInfo } from '../../api/postApi';
import './PostDetailPage.css';

function PostDetailPage() {
    const { postId } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);
    const [comments, setComments] = useState([]);
    const [commentInput, setCommentInput] = useState('');
    const [likeInfo, setLikeInfo] = useState({ liked: false, likeCount: 0 });
    const [loading, setLoading] = useState(true);

    const isLoggedIn = !!localStorage.getItem('accessToken');

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [postData, commentData, likeData] = await Promise.all([
                    getPost(postId),
                    getComments(postId),
                    getLikeInfo(postId)
                ]);
                setPost(postData);
                setComments(commentData);
                setLikeInfo(likeData);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [postId]);

    const handleLike = async () => {
        if (!isLoggedIn) { navigate('/login'); return; }
        try {
            const data = await toggleLike(postId);
            setLikeInfo(data);
        } catch (err) { console.error(err); }
    };

    const handleComment = async (e) => {
        e.preventDefault();
        if (!commentInput.trim()) return;
        try {
            const newComment = await createComment(postId, commentInput);
            setComments([...comments, newComment]);
            setCommentInput('');
        } catch (err) { console.error(err); }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            await deleteComment(postId, commentId);
            setComments(comments.filter(c => c.commentId !== commentId));
        } catch (err) { console.error(err); }
    };

    const handleDeletePost = async () => {
        if (!window.confirm('게시글을 삭제하시겠습니까?')) return;
        try {
            await deletePost(postId);
            navigate('/');
        } catch (err) { console.error(err); }
    };

    if (loading) return <div className="detail-container"><Navbar /><p className="loading">불러오는 중...</p></div>;

    return (
        <div className="detail-container">
            <Navbar />
            <div className="detail-content">

                {/* 게시글 헤더 */}
                <div className="detail-header">
                    <div className="detail-category">
                        <span className="post-category">{post.categoryName}</span>
                        {post.subCategoryName && <span className="post-subcategory">{post.subCategoryName}</span>}
                    </div>
                    <h1 className="detail-title">{post.title}</h1>
                    <div className="detail-meta">
                        <span>{post.nickname}</span>
                        <span>{new Date(post.createdAt).toLocaleDateString()}</span>
                        <span>조회 {post.viewCount}</span>
                    </div>
                </div>

                {/* 게시글 내용 */}
                <div className="detail-body">{post.content}</div>

                {/* 좋아요 */}
                <div className="detail-like">
                    <button className={`like-btn ${likeInfo.liked ? 'liked' : ''}`} onClick={handleLike}>
                        ❤️ {likeInfo.likeCount}
                    </button>
                </div>

                {/* 게시글 수정/삭제 */}
                {post.isMyPost && (
                    <div className="detail-actions">
                        <button onClick={() => navigate(`/posts/${postId}/edit`)} className="edit-btn">수정</button>
                        <button onClick={handleDeletePost} className="delete-btn">삭제</button>
                    </div>
                )}

                {/* 댓글 */}
                <div className="comment-section">
                    <h3 className="comment-title">댓글 {comments.length}</h3>
                    <div className="comment-list">
                        {comments.map(comment => (
                            <div key={comment.commentId} className="comment-item">
                                <div className="comment-header">
                                    <span className="comment-author">{comment.nickname}</span>
                                    <span className="comment-date">{new Date(comment.createdAt).toLocaleDateString()}</span>
                                    {comment.isMyComment && (
                                        <button onClick={() => handleDeleteComment(comment.commentId)} className="comment-delete">삭제</button>
                                    )}
                                </div>
                                <p className="comment-content">{comment.content}</p>
                            </div>
                        ))}
                    </div>

                    {/* 댓글 작성 */}
                    {isLoggedIn && (
                        <form onSubmit={handleComment} className="comment-form">
                            <input
                                type="text"
                                value={commentInput}
                                onChange={e => setCommentInput(e.target.value)}
                                placeholder="댓글을 입력하세요"
                                className="comment-input"
                            />
                            <button type="submit" className="comment-submit">등록</button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}

export default PostDetailPage;