import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { getRecord, deleteRecord } from '../../api/recordApi';
import { createPost } from '../../api/postApi';
import { getCategories } from '../../api/categoryApi';
import './RecordDetailPage.css';
import KakaoMap from '../../components/KakaoMap';

function RecordDetailPage() {
    const { recordId } = useParams();
    const navigate = useNavigate();
    const [record, setRecord] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showShareModal, setShowShareModal] = useState(false);
    const [categories, setCategories] = useState([]);
    const [shareForm, setShareForm] = useState({ categoryId: '', subCategoryId: '', title: '' });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [recordData, categoryData] = await Promise.all([
                    getRecord(recordId),
                    getCategories()
                ]);
                setRecord(recordData);
                setCategories(categoryData);
                setShareForm(prev => ({ ...prev, title: recordData.title }));
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, [recordId]);

    const handleDelete = async () => {
        if (!window.confirm('기록을 삭제하시겠습니까?')) return;
        try {
            await deleteRecord(recordId);
            navigate('/mypage');
        } catch (err) { console.error(err); }
    };

    // 게시글로 공유
    const handleShare = async (e) => {
        e.preventDefault();
        try {
            await createPost({
                title: shareForm.title,
                content: record.content || '',
                categoryId: parseInt(shareForm.categoryId),
                subCategoryId: shareForm.subCategoryId ? parseInt(shareForm.subCategoryId) : null,
                recordId: parseInt(recordId),
            });
            alert('게시글로 공유됐습니다!');
            setShowShareModal(false);
            navigate('/');
        } catch (err) { console.error(err); }
    };

    if (loading) return <div><Navbar /><p style={{ textAlign: 'center', padding: '60px' }}>불러오는 중...</p></div>;

    const selectedCategory = categories.find(c => c.categoryId === parseInt(shareForm.categoryId));

    return (
        <div className="record-detail-container">
            <Navbar />
            <div className="record-detail-content">

                {/* 헤더 */}
                <div className="record-detail-header">
                    <h1 className="record-detail-title">{record.title}</h1>
                    <div className="record-detail-date">
                        {new Date(record.startTime).toLocaleDateString()}
                    </div>
                </div>

                {/* 운동 통계 */}
                <div className="record-stats-card">
                    <div className="record-stat">
                        <span className="record-stat-value">{record.distanceKm}</span>
                        <span className="record-stat-label">km</span>
                    </div>
                    <div className="record-stat-divider" />
                    <div className="record-stat">
                        <span className="record-stat-value">{record.durationMin}</span>
                        <span className="record-stat-label">분</span>
                    </div>
                    <div className="record-stat-divider" />
                    <div className="record-stat">
                        <span className="record-stat-value">
                            {(record.distanceKm / (record.durationMin / 60)).toFixed(1)}
                        </span>
                        <span className="record-stat-label">km/h</span>
                    </div>
                </div>

                {/* GPX 경로 지도 */}
                {record.gpxData && (
                    <div className="record-map">
                        <KakaoMap gpxData={record.gpxData} height="350px" />
                    </div>
                )}

                {/* 사진 */}
                {record.attachments?.length > 0 && (
                    <div className="record-photos">
                        {record.attachments.map(att => (
                           <img key={att.fileId} src={`http://localhost:8081${att.filePath}`} alt={att.originalName} className="record-photo" />
                        ))}
                    </div>
                )}

                {/* 라이딩 일지 */}
                {record.content && (
                    <div className="record-diary">
                        <h3>라이딩 일지</h3>
                        <p>{record.content}</p>
                    </div>
                )}

                {/* 액션 버튼 */}
                <div className="record-actions">
                    <button className="share-btn" onClick={() => setShowShareModal(true)}>
                        📤 게시글로 공유
                    </button>
                    <button className="delete-btn" onClick={handleDelete}>삭제</button>
                </div>
            </div>

            {/* 공유 모달 */}
            {showShareModal && (
                <div className="modal-overlay" onClick={() => setShowShareModal(false)}>
                    <div className="modal" onClick={e => e.stopPropagation()}>
                        <h3 className="modal-title">게시글로 공유</h3>
                        <form onSubmit={handleShare} className="modal-form">
                            <input type="text" value={shareForm.title}
                                onChange={e => setShareForm({ ...shareForm, title: e.target.value })}
                                placeholder="게시글 제목" required />
                            <select value={shareForm.categoryId}
                                onChange={e => setShareForm({ ...shareForm, categoryId: e.target.value, subCategoryId: '' })}
                                required>
                                <option value="">카테고리 선택</option>
                                {categories.map(c => (
                                    <option key={c.categoryId} value={c.categoryId}>{c.name}</option>
                                ))}
                            </select>
                            {selectedCategory?.subCategories?.length > 0 && (
                                <select value={shareForm.subCategoryId}
                                    onChange={e => setShareForm({ ...shareForm, subCategoryId: e.target.value })}>
                                    <option value="">소분류 선택</option>
                                    {selectedCategory.subCategories.map(s => (
                                        <option key={s.subCategoryId} value={s.subCategoryId}>{s.subCategoryName}</option>
                                    ))}
                                </select>
                            )}
                            <div className="modal-buttons">
                                <button type="button" onClick={() => setShowShareModal(false)} className="cancel-btn">취소</button>
                                <button type="submit" className="submit-btn">공유</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default RecordDetailPage;