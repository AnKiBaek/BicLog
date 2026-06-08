package com.biclog.backend.domain.category.repository;

import com.biclog.backend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 카테고리명으로 조회 (중복 체크)
    Optional<Category> findByName(String name);

    // 카테고리명 존재 여부 확인
    boolean existsByName(String name);
}