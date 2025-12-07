package com.exemple.movieapp.service;


import com.exemple.movieapp.entities.Category;
import com.exemple.movieapp.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

}
