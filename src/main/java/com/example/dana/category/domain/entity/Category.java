package com.example.dana.category.domain.entity;

import com.example.dana.category.controller.request.CategoryRequest;
import com.example.dana.common.domain.BaseMetaDataEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseMetaDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    private Category(String name) {
        this.name = name;
    }

    public void addChildCategory(Category child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChildrenCategories(List<Category> children) {
        for (Category child : children) {
            addChildCategory(child);
        }
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public static Category createCategory(String name) {
        return new Category(name);
    }

    public static Category fromRequest(CategoryRequest request) {
        return createCategory(request.getName());
    }
}
