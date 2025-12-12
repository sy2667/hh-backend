package com.household.backend.controller;

import com.household.backend.common.SessionUtils;
import com.household.backend.dto.req.CategoryCreate;
import com.household.backend.dto.res.CategoryRes;
import com.household.backend.entity.Category;
import com.household.backend.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryRes> create(@RequestBody CategoryCreate req, HttpSession session) {
        Integer userPk = SessionUtils.getLoginUserPk(session);

        Category c = categoryService.createCategory(userPk, req);
        return ResponseEntity.ok(CategoryRes.from(c));
    }

    @GetMapping
    public ResponseEntity<List<CategoryRes>> listMyCategory(HttpSession session) {
        Integer userPk = SessionUtils.getLoginUserPk(session);

        List<CategoryRes> list = categoryService.findByUser(userPk);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/type")
    public ResponseEntity<List<CategoryRes>> listByType(@RequestParam("type") String categoryType, HttpSession session) {
        Integer userPk = SessionUtils.getLoginUserPk(session);
        List<CategoryRes> list = categoryService.findByUserAndType(userPk, categoryType);

        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{categoryPk}")
    public ResponseEntity<Void> delete(@PathVariable Integer categoryPk, HttpSession session) {
        Integer userPk = (Integer) session.getAttribute("USER");
        if (userPk == null) {
            return ResponseEntity.status(401).build();
        }

        categoryService.deleteCategory(categoryPk);
        return ResponseEntity.noContent().build();
    }


}
