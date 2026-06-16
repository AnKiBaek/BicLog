package com.biclog.backend.domain.record.controller;

import com.biclog.backend.domain.record.dto.RecordRequestDto;
import com.biclog.backend.domain.record.dto.RecordResponseDto;
import com.biclog.backend.domain.record.service.RecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    // 라이딩 기록 작성 POST /api/records
    @PostMapping
    public ResponseEntity<RecordResponseDto.Detail> createRecord(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestPart RecordRequestDto.Create request,
            @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.createRecord(userId, request, files));
    }

    // 내 라이딩 기록 목록 조회 GET /api/records/me
    @GetMapping("/me")
    public ResponseEntity<Page<RecordResponseDto.Summary>> getMyRecords(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(recordService.getMyRecords(userId, pageable));
    }

    // 라이딩 기록 상세 조회 GET /api/records/{recordId}
    @GetMapping("/{recordId}")
    public ResponseEntity<RecordResponseDto.Detail> getRecord(@PathVariable Long recordId) {
        return ResponseEntity.ok(recordService.getRecord(recordId));
    }

    // 라이딩 기록 삭제 DELETE /api/records/{recordId}
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long recordId) {
        Long userId = Long.parseLong(userDetails.getUsername());
        recordService.deleteRecord(userId, recordId);
        return ResponseEntity.noContent().build();
    }
}