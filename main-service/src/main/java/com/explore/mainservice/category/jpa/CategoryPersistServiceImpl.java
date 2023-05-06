package com.explore.mainservice.category.jpa;

import com.explore.mainservice.category.model.Category;
import com.explore.mainservice.category.repository.CategoryRepository;
import com.explore.mainservice.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryPersistServiceImpl implements CategoryPersistService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> findCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from, size));
    }

    public Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("not found category " +
                        " with id %d", catId), "try to put this category first"));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new NotFoundException(String.format("not found category " +
                        " with name %s", name), "try to put this category first"));
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }
}

