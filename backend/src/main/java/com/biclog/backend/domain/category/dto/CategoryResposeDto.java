package com.biclog.backend.domain.category.dto;

import com.biclog.backend.domain.category.entity.Category;
import com.biclog.backend.domain.category.entity.SubCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class CategoryResposeDto {
    // 게시글 작성시 드롭다운 메뉴 응답
    @Getter
    @Builder
    public  static class CategoryTitle {
        private Long categoryId;
        private String name;
        private List<SubCategoryTitle> subCategories;

        public  static  CategoryTitle from(Category category, List<SubCategoryTitle> subCategories){
            return CategoryTitle.builder()
                    .categoryId(category.getCategoryId())
                    .name(category.getName())
                    .subCategories(subCategories)
                    .build();
        }

        // 카테고리 소분류 목록 응답
        @Getter
        @Builder
        public static class SubCategoryTitle{
            private  Long subCategoryId;
            private String subCategoryName;

            public static SubCategoryTitle from(SubCategory subCategory) {
                return SubCategoryTitle.builder()
                        .subCategoryId(subCategory.getSubCategoryId())
                        .subCategoryName(subCategory.getSubCategoryName())
                        .build();
            }
        }

    }


}
