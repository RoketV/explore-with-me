package com.explore.mainservice.category.jpa;

import com.explore.mainservice.category.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryPersistService {

    Page<Category> findCategories(Integer from, Integer size);

    List<Category> findAll();

    Optional<Category> findCategoryById(Long catId);

    Category findCategoryByName(String name);

    Category addCategory(Category category);

    Category updateCategory(Category category);

    void deleteCategory(Long catId);
}
