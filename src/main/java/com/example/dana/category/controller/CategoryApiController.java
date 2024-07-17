package com.example.dana.category.controller;

import com.example.dana.category.controller.request.CategoryRequest;
import com.example.dana.category.controller.response.CategoryResponse;
import com.example.dana.category.service.CategoryService;
import com.example.dana.common.response.ResponseWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.dana.category.constants.CategorySuccessMessage.ADD_CATEGORY_SUCCESS;
import static com.example.dana.category.constants.CategorySuccessMessage.UPDATE_CATEGORY_SUCCESS;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseWrapper<CategoryResponse> addParentCategory(@RequestBody @Valid CategoryRequest request) {
        return ResponseWrapper.SUCCESS(ADD_CATEGORY_SUCCESS, categoryService.addParentCategory(request));
    }

    @PostMapping("/{parentId}/children")
    public ResponseWrapper<CategoryResponse> addChildrenCategories(@PathVariable(name = "parentId") Long parentId, @RequestBody List<CategoryRequest> requests) {
        return ResponseWrapper.SUCCESS(ADD_CATEGORY_SUCCESS, categoryService.addChildrenCategories(parentId, requests));
    }

    @PatchMapping("/{categoryId}")
    public ResponseWrapper<CategoryResponse> updateCategory(@PathVariable(name = "categoryId") Long categoryId, @RequestBody @Valid CategoryRequest request) {
        return ResponseWrapper.SUCCESS(UPDATE_CATEGORY_SUCCESS, categoryService.updateCategory(categoryId, request));
    }

    @GetMapping
    public ResponseWrapper<List<CategoryResponse>> getAllCategories() {
        return ResponseWrapper.SUCCESS(null, categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseWrapper<CategoryResponse> getCategories(@PathVariable(name = "categoryId") Long categoryId) {
        return ResponseWrapper.SUCCESS(null, categoryService.getCategoriesByCategoryId(categoryId));
    }

    @GetMapping("/{parentId}/children")
    public ResponseWrapper<List<CategoryResponse>> getChildrenCategories(@PathVariable(name = "parentId") Long parentId) {
        return ResponseWrapper.SUCCESS(null, categoryService.getChildrenCategoriesByParentId(parentId));
    }
}
