package com.vasylyshyn.finanseTracker.Mappers;

import com.vasylyshyn.finanseTracker.Entitys.Category;
import com.vasylyshyn.finanseTracker.dtos.CategoryResponseDTO;

public class CategoryMapper {
    public static CategoryResponseDTO toDto(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        return dto;
    }
}
