package com.household.backend.dto.res;

import com.household.backend.entity.Category;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CategoryRes {
    private Integer categoryPk;
    private String categoryName;
    private String categoryType;
    private LocalDateTime createdAt;

    public static CategoryRes from(Category c) {
        return CategoryRes.builder()
                .categoryPk(c.getCategoryPk())
                .categoryName(c.getCategoryName())
                .categoryType(c.getCategoryType())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
