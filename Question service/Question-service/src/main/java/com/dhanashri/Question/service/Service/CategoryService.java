package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.Dao.CategoryDao;
import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Exception.ResourceNotFoundException;
import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.DTO.Response.CategoryStatsResponse;
import com.dhanashri.Question.service.DTO.Response.GenerateQuizCategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Autowired
    QuestionDao questionDao;

    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    public List<CategoryStatsResponse> getCategoryStats() {
        return categoryDao.getCategoryStats();
    }

    public List<GenerateQuizCategoryDTO> getActiveQuestionCountByCategory() {
        return categoryDao.getGenerateQuizInventory();
    }

    public void addCategory(Category category) {
        categoryDao.save(category);
    }

    public void deleteCategory(int categoryId) {
        if (!categoryDao.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with ID: " + categoryId);
        }
        categoryDao.deleteById(categoryId);
    }

    public void updateCategory(int categoryId, Category updatedCategory) {
        Category existingCategory = categoryDao.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with Id:-"+categoryId));

        existingCategory.setCategory(updatedCategory.getCategory());
        categoryDao.save(existingCategory);
    }
}
