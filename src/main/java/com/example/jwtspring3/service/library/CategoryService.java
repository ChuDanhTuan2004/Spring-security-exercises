package com.example.jwtspring3.service.library;

import com.example.jwtspring3.model.library.Book;
import com.example.jwtspring3.model.library.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category saveCategory(Category category);
    void deleteCategory(Long id);

    List<Category> searchCategoriesByName(String name);
}
