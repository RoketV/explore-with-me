package com.explore.mainservice.category.mapper;

import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.dto.NewCategoryDto;
import com.explore.mainservice.category.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    CategoryDto mapToCategoryDto(Category category);

    Category toMapCategory(NewCategoryDto newCategoryDto);
}
