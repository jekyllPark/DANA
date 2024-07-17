package com.example.dana.category.controller.response;

import com.example.dana.category.domain.entity.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryResponse> children = new ArrayList<>();
    public static CategoryResponse fromEntity(Category category) {
        List<CategoryResponse> childrenResponses = null;
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            childrenResponses = category.getChildren().stream()
                    .map(CategoryResponse::fromEntity)
                    .collect(Collectors.toList());
        }
        Long parentId = (category.getParent() != null) ? category.getParent().getId() : null;

        return new CategoryResponse(category.getId(), category.getName(), parentId, childrenResponses);
    }
}
