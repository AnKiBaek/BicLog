package com.biclog.backend.domain.record.service;

import com.biclog.backend.domain.attachment.entity.Attachment;
import com.biclog.backend.domain.attachment.repository.AttachmentRepository;
import com.biclog.backend.domain.record.dto.RecordRequestDto;
import com.biclog.backend.domain.record.dto.RecordResponseDto;
import com.biclog.backend.domain.record.entity.Record;
import com.biclog.backend.domain.record.repository.RecordRepository;
import com.biclog.backend.domain.user.entity.User;
import com.biclog.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    private final String UPLOAD_DIR = "uploads/"; // 파일 저장 경로

    // 라이딩 기록 작성
    @Transactional
    public RecordResponseDto.Detail createRecord(Long userId, RecordRequestDto.Create request,
                                                 List<MultipartFile> files) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다"));

        Record record = Record.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationMin(request.getDurationMin())
                .distanceKm(request.getDistanceKm())
                .gpxData(request.getGpxData())
                .build();

        Record saved = recordRepository.save(record);

        // 사진 저장
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                saveFile(saved, file);
            }
        }

        List<RecordResponseDto.AttachmentInfo> attachments = getAttachments(saved.getRecordId());
        return RecordResponseDto.Detail.from(saved, attachments);
    }

    // 내 라이딩 기록 목록 조회
    public Page<RecordResponseDto.Summary> getMyRecords(Long userId, Pageable pageable) {
        return recordRepository.findByUserUserId(userId, pageable)
                .map(record -> {
                    List<Attachment> files = attachmentRepository.findByRecordRecordId(record.getRecordId());
                    String thumbnailUrl = files.isEmpty() ? null : files.get(0).getFilePath();
                    return RecordResponseDto.Summary.from(record, thumbnailUrl);
                });
    }

    // 라이딩 기록 상세 조회
    public RecordResponseDto.Detail getRecord(Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다"));
        List<RecordResponseDto.AttachmentInfo> attachments = getAttachments(recordId);
        return RecordResponseDto.Detail.from(record, attachments);
    }

    // 라이딩 기록 삭제
    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기록입니다"));

        if (!record.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 기록만 삭제할 수 있습니다");
        }

        recordRepository.delete(record);
    }

    // 파일 저장
    private void saveFile(Record record, MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originalName;
        Path path = Paths.get(UPLOAD_DIR + storedName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Attachment attachment = Attachment.builder()
                .record(record)
                .originalName(originalName)
                .storedName(storedName)
                .filePath("/" + UPLOAD_DIR + storedName)
                .fileSize(file.getSize())
                .build();

        attachmentRepository.save(attachment);
    }

    // 첨부파일 목록 조회
    private List<RecordResponseDto.AttachmentInfo> getAttachments(Long recordId) {
        return attachmentRepository.findByRecordRecordId(recordId)
                .stream()
                .map(RecordResponseDto.AttachmentInfo::from)
                .toList();
    }
}