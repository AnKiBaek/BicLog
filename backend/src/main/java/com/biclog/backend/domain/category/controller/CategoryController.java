package com.biclog.backend.domain.category.controller;

import com.biclog.backend.domain.category.dto.CategoryResposeDto;
import com.biclog.backend.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 목록 조회 GET /api/categories
    @GetMapping
    public ResponseEntity<List<CategoryResposeDto.CategoryTitle>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }
}