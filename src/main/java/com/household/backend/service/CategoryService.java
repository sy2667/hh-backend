package com.household.backend.service;

import com.household.backend.dto.req.CategoryCreate;
import com.household.backend.dto.res.CategoryRes;
import com.household.backend.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    // 카테고리 생성
    Category createCategory(Integer userPk, CategoryCreate categoryCreate);

    // 카테고리 조회 (ID로)
    Optional<Category> findById(Integer categoryPk);

    // 사용자의 모든 카테고리 조회
    List<CategoryRes> findByUser(Integer userPk);

    // 사용자의 특정 타입 카테고리 조회 (수입 또는 지출)
    List<CategoryRes> findByUserAndType(Integer userPk, String categoryType);

    // 카테고리 수정
    Category updateCategory(Integer categoryPk, String categoryName);

    // 카테고리 삭제
    void deleteCategory(Integer categoryPk);

}
