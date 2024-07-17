package com.example.dana.category.domain.repository;

import com.example.dana.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByActiveTrueAndParentIsNull();
    List<Category> findByParentIdAndActiveTrue(Long parentId);
    Category findByActiveTrueAndId(Long categoryId);
}
