package com.biclog.backend.domain.record.repository;

import com.biclog.backend.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordRepository extends JpaRepository<Record, Long> {

    // 특정 유저의 라이딩 기록 목록 (페이징)
    Page<Record> findByUserUserId(Long userId, Pageable pageable);

    // 특정 유저의 라이딩 기록 개수
    long countByUserUserId(Long userId);
}