package com.explore.mainservice.category.service;

import com.explore.mainservice.category.dto.CategoryDto;
import com.explore.mainservice.category.dto.NewCategoryDto;
import com.explore.mainservice.category.jpa.CategoryPersistService;
import com.explore.mainservice.category.mapper.CategoryMapper;
import com.explore.mainservice.category.model.Category;
import com.explore.mainservice.exceptions.BadRequestException;
import com.explore.mainservice.exceptions.ConflictException;
import com.explore.mainservice.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryPersistService categoryPersistService;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> categories = categoryPersistService.findCategories(from, size).getContent();

        if (categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories.stream()
                .map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long catId) {

        Category category = categoryPersistService.findCategoryById(catId);

        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {

        if (newCategoryDto.getName() == null) {
            throw new BadRequestException("Bad request body", "Category name is empty");
        }
        Category categoryCheck;
        try {
            categoryCheck = categoryPersistService.findCategoryByName(newCategoryDto.getName());
        } catch (NotFoundException e) {
            categoryCheck = null;
        }

        if (categoryCheck != null && categoryCheck.getName().equals(newCategoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        categoryMapper.toMapCategory(newCategoryDto);

        Category category = categoryPersistService.addCategory(categoryMapper.toMapCategory(newCategoryDto));

        return categoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategory(Long catId) {

        Category category = categoryPersistService.findCategoryById(catId);

        if (category.getEvents() != null && category.getEvents().size() > 0) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The category is not empty");
        }

        categoryPersistService.deleteCategory(catId);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {

        if (categoryDto.getName() == null) {
            throw new BadRequestException("Bad request body", "Category name is empty");
        }
        Category categoryCheck;
        try {
            categoryCheck = categoryPersistService.findCategoryByName(categoryDto.getName());
        } catch (NotFoundException e) {
            categoryCheck = null;
        }

        if (categoryCheck != null && categoryCheck.getName().equals(categoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name]; " +
                            "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                            "could not execute statement");
        }

        Category category = categoryPersistService.findCategoryById(catId);


        category.setName(categoryDto.getName());

        return categoryMapper.mapToCategoryDto(categoryPersistService.updateCategory(category));
    }
}

