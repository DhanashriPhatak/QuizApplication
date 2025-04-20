package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.CategoryStatsResponse;
import com.dhanashri.Question.service.Module.Response;
import com.dhanashri.Question.service.Service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /*
    Description: Get List of all categories available in Database
     */
    @GetMapping("getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories()
    {
        return categoryService.getAllCategories();
    }

    @GetMapping("getCategoryStats")
    public ResponseEntity<List<CategoryStatsResponse>> getCategoryStats()
    {
        return categoryService.getCategoryStats();
    }
}
