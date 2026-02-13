package com.household.backend.controller;

import com.household.backend.dto.req.CategoryCreate;
import com.household.backend.dto.res.CategoryRes;
import com.household.backend.entity.Category;
import com.household.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryRes> create(@RequestBody CategoryCreate req, @AuthenticationPrincipal Integer userPk) {
        Category c = categoryService.createCategory(userPk, req);
        return ResponseEntity.ok(CategoryRes.from(c));
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> listMyCategory(@RequestBody @AuthenticationPrincipal Integer userPk) {
        List<CategoryRes> list = categoryService.findByUser(userPk);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/type")
    public ResponseEntity<List<CategoryRes>> listByType(@RequestParam("type") String categoryType, @AuthenticationPrincipal Integer userPk) {
        List<CategoryRes> list = categoryService.findByUserAndType(userPk, categoryType);

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{categoryPk}")
    public ResponseEntity<Void> delete(@PathVariable Integer categoryPk, @AuthenticationPrincipal Integer userPk) {
        categoryService.deleteCategory(categoryPk);
        return ResponseEntity.noContent().build();
    }


}
