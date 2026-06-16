import { useEffect, useRef } from 'react';

const KAKAO_MAP_KEY = '14fb4099e2beecebfb097509fef9d412';

function KakaoMap({ gpxData, height = '300px' }) {
    const mapRef = useRef(null);

    useEffect(() => {
        console.log('gpxData:', gpxData);
        if (!gpxData) return;

        // GPX 데이터에서 좌표 추출 (lat,lng 쌍의 배열로 가정)
        let coordinates = [];
        try {
            coordinates = JSON.parse(gpxData); // [{lat: 37.5, lng: 127.0}, ...]
        } catch (err) {
            console.error('GPX 데이터 파싱 실패', err);
            return;
        }

        if (!coordinates || coordinates.length === 0) return;

        // 카카오맵 SDK 로드
        const loadKakaoMap = () => {
             console.log('loadKakaoMap 호출, window.kakao:', window.kakao);
            if (window.kakao && window.kakao.maps) {
                drawMap();
                return;
            }
            const script = document.createElement('script');
            script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${KAKAO_MAP_KEY}&autoload=false`;
            script.onload = () => {
                window.kakao.maps.load(drawMap);
            };
            document.head.appendChild(script);
        };

        const drawMap = () => {
            console.log('drawMap 실행, mapRef:', mapRef.current); 
            const center = new window.kakao.maps.LatLng(coordinates[0].lat, coordinates[0].lng);
            const map = new window.kakao.maps.Map(mapRef.current, {
                center,
                level: 5,
            });

            // 경로 좌표 배열
            const path = coordinates.map(c => new window.kakao.maps.LatLng(c.lat, c.lng));

            // 경로 선 그리기
            const polyline = new window.kakao.maps.Polyline({
                path,
                strokeWeight: 4,
                strokeColor: '#2563eb',
                strokeOpacity: 0.8,
                strokeStyle: 'solid',
            });
            polyline.setMap(map);

            // 시작점 마커
            new window.kakao.maps.Marker({
                position: path[0],
                map,
            });

            // 끝점 마커
            new window.kakao.maps.Marker({
                position: path[path.length - 1],
                map,
            });

            // 경로 전체가 보이도록 영역 설정
            const bounds = new window.kakao.maps.LatLngBounds();
            path.forEach(p => bounds.extend(p));
            map.setBounds(bounds);
        };

        loadKakaoMap();
    }, [gpxData]);

    if (!gpxData) return null;

    return <div ref={mapRef} style={{ width: '100%', height, borderRadius: '8px' }} />;
}

export default KakaoMap;