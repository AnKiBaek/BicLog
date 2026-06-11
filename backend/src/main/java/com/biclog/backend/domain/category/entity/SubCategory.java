package com.biclog.backend.domain.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SUB_CATEGORIES")
@Getter
@NoArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "SUB_CATEGORY_ID")
    private  Long subCategoryId;

    @ManyToOne (fetch =FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID",nullable = false)
    private Category category;

    @Column(name = "NAME",nullable = false)
    private String subCategoryName;

}
