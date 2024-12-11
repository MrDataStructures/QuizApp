package com.beaconfire.quizapp.service;

import com.beaconfire.quizapp.model.Category;
import com.beaconfire.quizapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Integer> getIdByName(String name) {
        return categoryRepository.getIdByName(name);
    }

    public Optional<Category> getCategoryById(int id) {
        return Optional.ofNullable(categoryRepository.getCategoryById(id));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    public boolean addCategory(Category category) {
        if (categoryRepository.getIdByName(category.getName()).isPresent()) {
            return false;
        }
        categoryRepository.createCategory(category);
        return true;
    }

    public boolean updateCategory(Category category) {
        Optional<Category> existingCategory = Optional.ofNullable(categoryRepository.getCategoryById(category.getCategory_id()));
        if (existingCategory.isPresent()) {
            categoryRepository.updateCategory(category);
            return true;
        }
        return false;
    }

    public boolean deleteCategoryById(int id) {
        Optional<Category> category = Optional.ofNullable(categoryRepository.getCategoryById(id));
        if (category.isPresent()) {
            categoryRepository.deleteCategory(id);
            return true;
        }
        return false;
    }
}
