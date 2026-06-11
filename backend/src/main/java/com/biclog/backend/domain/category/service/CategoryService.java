package com.biclog.backend.domain.category.service;

import com.biclog.backend.domain.category.dto.CategoryResposeDto;
import com.biclog.backend.domain.category.entity.Category;
import com.biclog.backend.domain.category.repository.CategoryRepository;
import com.biclog.backend.domain.category.repository.SubCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;


    public List<CategoryResposeDto.CategoryTitle> getCategories() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    List<CategoryResposeDto.CategoryTitle.SubCategoryTitle> subCategories =
                            subCategoryRepository.findByCategoryCategoryId(category.getCategoryId())
                                    .stream()
                                    .map(CategoryResposeDto.CategoryTitle.SubCategoryTitle::from)
                                    .toList();
                    return CategoryResposeDto.CategoryTitle.from(category, subCategories);
                })
                .toList();
    }
}
