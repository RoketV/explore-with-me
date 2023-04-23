package com.explore.mainservice.category.service;

import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);
}
