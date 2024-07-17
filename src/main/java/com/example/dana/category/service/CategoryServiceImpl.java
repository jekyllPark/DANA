package com.example.dana.category.service;

import com.example.dana.category.controller.request.CategoryRequest;
import com.example.dana.category.controller.response.CategoryResponse;
import com.example.dana.category.domain.entity.Category;
import com.example.dana.category.domain.repository.CategoryRepository;
import com.example.dana.common.exception.UserHandleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.dana.category.constants.CategoryErrorMessage.ALREADY_DELETED_CATEGORY_EXCEPTION;
import static com.example.dana.category.constants.CategoryErrorMessage.NOT_FOUND_CATEGORY_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryResponse addParentCategory(CategoryRequest request) {
        Category savedCategory = categoryRepository.save(Category.fromRequest(request));
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    @Override
    public CategoryResponse addChildrenCategories(Long parentId, List<CategoryRequest> requests) {
        Category parentCategory = findByCategoryId(parentId);

        validateActivation(parentCategory);

        List<Category> childCategories = requests.stream()
                .map(Category::fromRequest)
                .collect(Collectors.toList());

        parentCategory.addChildrenCategories(childCategories);
        Category savedCategory = categoryRepository.save(parentCategory);
        return CategoryResponse.fromEntity(savedCategory);
    }

    private Category findByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new UserHandleException(NOT_FOUND_CATEGORY_EXCEPTION));
        return category;
    }

    private static void validateActivation(Category category) {
        if (!category.isActive()) {
            throw new UserHandleException(ALREADY_DELETED_CATEGORY_EXCEPTION);
        }
    }

    @Transactional
    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
        Category category = findByCategoryId(categoryId);

        validateActivation(category);

        category.updateName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAllByActiveTrueAndParentIsNull();
        return categories
                .stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoriesByCategoryId(Long categoryId) {
        Category category = categoryRepository.findByActiveTrueAndId(categoryId);
        return CategoryResponse.fromEntity(category);
    }

    @Override
    public List<CategoryResponse> getChildrenCategoriesByParentId(Long parentId) {
        List<Category> categories = categoryRepository.findByParentIdAndActiveTrue(parentId);
        return categories
                .stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
