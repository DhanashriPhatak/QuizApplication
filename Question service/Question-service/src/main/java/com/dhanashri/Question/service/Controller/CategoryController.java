package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.CategoryStatsResponse;
import com.dhanashri.Question.service.Module.Response;
import com.dhanashri.Question.service.Service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**    Description: Get List of all categories available in Database    */
    @GetMapping("getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories()
    {
        return categoryService.getAllCategories();
    }

    /** Description: This is to get category Stats*/
    @GetMapping("getCategoryStats")
    public ResponseEntity<List<CategoryStatsResponse>> getCategoryStats()
    {
        return categoryService.getCategoryStats();
    }

    /** Description:- This is to Add a category*/
    @PostMapping("add")
    public ResponseEntity<String> addCategory(@RequestBody Category category)
    {
        return  categoryService.addCategory(category);
    }

    /** Description:- This is to Delete a category*/
    @PostMapping("deleteCategory/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId)
    {
        return  categoryService.deleteCategory(categoryId);
    }
}
