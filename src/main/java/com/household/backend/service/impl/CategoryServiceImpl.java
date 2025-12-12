package com.household.backend.service.impl;

import com.household.backend.dto.req.CategoryCreate;
import com.household.backend.dto.res.CategoryRes;
import com.household.backend.entity.Category;
import com.household.backend.entity.User;
import com.household.backend.repository.CategoryRepository;
import com.household.backend.repository.UserRepository;
import com.household.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Category createCategory(Integer userPk, CategoryCreate categoryCreate) {
        User user = userRepository.findById(userPk).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Category category = new Category();
        category.setUser(user);
        category.setCategoryName(categoryCreate.getCategoryName());
        category.setCategoryType(categoryCreate.getCategoryType());

        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Integer categoryPk) {
        return categoryRepository.findById(categoryPk);
    }

    @Override
    public List<CategoryRes> findByUser(Integer userPk) {
        return categoryRepository.findByUser(userPk).stream().map(CategoryRes::from).toList();
    }

    @Override
    public List<CategoryRes> findByUserAndType(Integer userPk, String categoryType) {
        return categoryRepository.findByUserAndType(userPk, categoryType).stream().map(CategoryRes::from).toList();
    }

    @Override
    @Transactional
    public Category updateCategory(Integer categoryPk, String categoryName) {
        Category category = categoryRepository.findById(categoryPk).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        category.setCategoryName(categoryName);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryPk) {
        Category category = categoryRepository.findById(categoryPk).orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        categoryRepository.delete(category);
    }

}
