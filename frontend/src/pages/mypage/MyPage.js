import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { getMyInfo } from '../../api/authApi';
import { getMyPosts } from '../../api/postApi';
import { getMyRecords } from '../../api/recordApi';
import './MyPage.css';

function MyPage() {
    const navigate = useNavigate();
    const [userInfo, setUserInfo] = useState(null);
    const [activeTab, setActiveTab] = useState('posts'); // 'posts' | 'records'
    const [posts, setPosts] = useState([]);
    const [records, setRecords] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const user = await getMyInfo();
                setUserInfo(user);
               const postData = await getMyPosts(); 
                setPosts(postData.content);
                const recordData = await getMyRecords();
                setRecords(recordData.content);
            } catch (err) {
                console.error(err);
                navigate('/login');
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    if (loading) return <div className="mypage-container"><Navbar /><p className="loading">불러오는 중...</p></div>;

    return (
        <div className="mypage-container">
            <Navbar />
            <div className="mypage-content">

                {/* 프로필 섹션 */}
                <div className="profile-section">
                    <div className="profile-avatar">
                        {userInfo?.nickname?.charAt(0).toUpperCase()}
                    </div>
                    <div className="profile-info">
                        <h2 className="profile-nickname">{userInfo?.nickname}</h2>
                        <div className="profile-stats">
                            <div className="stat-item">
                                <span className="stat-number">{posts.length}</span>
                                <span className="stat-label">게시글</span>
                            </div>
                            <div className="stat-item">
                                <span className="stat-number">{records.length}</span>
                                <span className="stat-label">라이딩</span>
                            </div>
                            <div className="stat-item">
                                <span className="stat-number">
                                    {records.reduce((sum, r) => sum + (r.distanceKm || 0), 0).toFixed(1)}
                                </span>
                                <span className="stat-label">총km</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* 탭 */}
                <div className="tab-bar">
                    <button
                        className={`tab-btn ${activeTab === 'posts' ? 'active' : ''}`}
                        onClick={() => setActiveTab('posts')}>
                        📝 게시글
                    </button>
                    <button
                        className={`tab-btn ${activeTab === 'records' ? 'active' : ''}`}
                        onClick={() => setActiveTab('records')}>
                        🚴 라이딩 기록
                    </button>
                </div>

                {/* 게시글 탭 */}
                {activeTab === 'posts' && (
                    <div className="grid-section">
                        {posts.length === 0 ? (
                            <p className="empty-text">작성한 게시글이 없어요</p>
                        ) : (
                            <div className="post-grid">
                                {posts.map(post => (
                                    <div key={post.postId} className="grid-item"
                                        onClick={() => navigate(`/posts/${post.postId}`)}>
                                        <div className="grid-item-category">{post.categoryName}</div>
                                        <div className="grid-item-title">{post.title}</div>
                                        <div className="grid-item-date">
                                            {new Date(post.createdAt).toLocaleDateString()}
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                )}

                {/* 라이딩 기록 탭 */}
                {activeTab === 'records' && (
                    <div className="grid-section">
                        <button className="add-record-btn" onClick={() => navigate('/records/write')}>
                            + 라이딩 기록 추가
                        </button>
                        {records.length === 0 ? (
                            <p className="empty-text">라이딩 기록이 없어요</p>
                        ) : (
                            <div className="record-grid">
                                {records.map(record => (
                                    <div key={record.recordId} className="record-grid-item"
                                        onClick={() => navigate(`/records/${record.recordId}`)}>
                                        <div className="record-thumbnail">
                                           {record.thumbnailUrl
                                             ? <img src={`http://localhost:8081${record.thumbnailUrl}`} alt="썸네일" />
                                             : <div className="record-no-img">🚴</div>}
                                        </div>
                                        <div className="record-grid-info">
                                            <div className="record-grid-title">{record.title}</div>
                                            <div className="record-grid-stats">
                                                {record.distanceKm}km · {record.durationMin}분
                                            </div>
                                            <div className="record-grid-date">
                                                {new Date(record.startTime).toLocaleDateString()}
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
}

export default MyPage;