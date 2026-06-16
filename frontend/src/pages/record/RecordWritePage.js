import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { createRecord } from '../../api/recordApi';
import './RecordWritePage.css';
import { parseGpxFileToJson } from '../../utils/gpxParser';

function RecordWritePage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        title: '',
        content: '',
        startTime: '',
        endTime: '',
        durationMin: '',
        distanceKm: '',
        gpxData: '',
    });
    const [files, setFiles] = useState([]);
    const [previews, setPreviews] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleFiles = (e) => {
        const selected = Array.from(e.target.files);
        setFiles(selected);
        setPreviews(selected.map(f => URL.createObjectURL(f)));
    };

    const handleGpxFile = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    try {
        const gpxJson = await parseGpxFileToJson(file);
        setForm({ ...form, gpxData: gpxJson });
    } catch (err) {
        console.error('GPX 파싱 실패', err);
    }
};

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await createRecord({
                ...form,
                durationMin: parseInt(form.durationMin),
                distanceKm: parseFloat(form.distanceKm),
            }, files);
            navigate('/mypage');
        } catch (err) {
            setError(err.response?.data?.message || '기록 저장에 실패했습니다');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="record-write-container">
            <Navbar />
            <div className="record-write-content">
                <h2 className="record-write-title">라이딩 기록 추가</h2>

                <form onSubmit={handleSubmit} className="record-write-form">

                    {/* 제목 */}
                    <div className="form-group">
                        <label>제목</label>
                        <input type="text" name="title" value={form.title}
                            onChange={handleChange} placeholder="라이딩 제목" required />
                    </div>

                    {/* 운동 정보 */}
                    <div className="form-row">
                        <div className="form-group">
                            <label>시작 시간</label>
                            <input type="datetime-local" name="startTime"
                                value={form.startTime} onChange={handleChange} required />
                        </div>
                        <div className="form-group">
                            <label>종료 시간</label>
                            <input type="datetime-local" name="endTime"
                                value={form.endTime} onChange={handleChange} required />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>거리 (km)</label>
                            <input type="number" step="0.01" name="distanceKm"
                                value={form.distanceKm} onChange={handleChange}
                                placeholder="0.00" required />
                        </div>
                        <div className="form-group">
                            <label>운동 시간 (분)</label>
                            <input type="number" name="durationMin"
                                value={form.durationMin} onChange={handleChange}
                                placeholder="0" required />
                        </div>
                    </div>

                    {/* 라이딩 중 있었던 일 */}
                    <div className="form-group">
                        <label>라이딩 일지</label>
                        <textarea name="content" value={form.content}
                            onChange={handleChange}
                            placeholder="라이딩 중 있었던 일을 기록해보세요" />
                    </div>

                    <div className="form-group">
                        <label>GPX 파일</label>
                        <input type="file" accept=".gpx" onChange={handleGpxFile} />
                         {form.gpxData && <p style={{fontSize: '13px', color: '#2563eb'}}>✅ GPX 파일이 업로드됐습니다</p>}
                    </div>

                    {/* 사진 업로드 */}
                    <div className="form-group">
                        <label>사진</label>
                        <input type="file" accept="image/*" multiple onChange={handleFiles} />
                        {previews.length > 0 && (
                            <div className="preview-grid">
                                {previews.map((src, i) => (
                                    <img key={i} src={src} alt={`preview-${i}`} className="preview-img" />
                                ))}
                            </div>
                        )}
                    </div>

                    {error && <p className="error-message">{error}</p>}

                    <div className="form-buttons">
                        <button type="button" className="cancel-btn" onClick={() => navigate('/mypage')}>취소</button>
                        <button type="submit" className="submit-btn" disabled={loading}>
                            {loading ? '저장 중...' : '저장'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default RecordWritePage;