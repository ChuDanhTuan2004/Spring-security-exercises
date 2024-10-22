package com.example.jwtspring3.service.library.impl;

import com.example.jwtspring3.model.library.Category;
import com.example.jwtspring3.repository.library.CategoryRepository;
import com.example.jwtspring3.service.library.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll()  ;
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id)  ;
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category)  ;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id)  ;
    }

    @Override
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
}
