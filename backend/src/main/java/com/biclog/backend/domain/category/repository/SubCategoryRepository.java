package com.biclog.backend.domain.category.repository;

import com.biclog.backend.domain.category.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {

    List<SubCategory>findByCategoryCategoryId(Long categoryId);
}
