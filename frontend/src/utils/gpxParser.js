// GPX XML 파일을 좌표 배열로 파싱
export const parseGpxFile = (gpxText) => {
    const parser = new DOMParser();
    const xmlDoc = parser.parseFromString(gpxText, 'text/xml');

    // trkpt (track point) 태그에서 lat, lon 추출
    const trkpts = xmlDoc.getElementsByTagName('trkpt');
    const coordinates = [];

    for (let i = 0; i < trkpts.length; i++) {
        const lat = parseFloat(trkpts[i].getAttribute('lat'));
        const lng = parseFloat(trkpts[i].getAttribute('lon'));
        if (!isNaN(lat) && !isNaN(lng)) {
            coordinates.push({ lat, lng });
        }
    }

    return coordinates;
};

// 파일 객체를 받아서 좌표 배열(JSON 문자열)로 변환
export const parseGpxFileToJson = (file) => {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => {
            try {
                const coordinates = parseGpxFile(e.target.result);
                resolve(JSON.stringify(coordinates));
            } catch (err) {
                reject(err);
            }
        };
        reader.onerror = reject;
        reader.readAsText(file);
    });
};