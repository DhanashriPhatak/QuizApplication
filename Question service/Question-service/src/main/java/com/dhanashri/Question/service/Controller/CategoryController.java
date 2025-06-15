package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.DTO.Response.CategoryStatsResponse;
import com.dhanashri.Question.service.Service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
@Tag(name = "Category API", description = "APIs to manage and retrieve Question's Category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Fetch all categories",
            description = "Returns a list of all categories available in the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping("getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories()
    {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Fetch category statistics",
            description = "Returns statistics for each category such as the number of questions, difficulty levels, etc."    )
    @GetMapping("getCategoryStats")
    public ResponseEntity<List<CategoryStatsResponse>> getCategoryStats()
    {
        return categoryService.getCategoryStats();
    }

    @Operation(summary = "Get count of active questions per category",
            description = "Returns the number of active questions available in each category.")
    @GetMapping("getActiveQuestionCountByCategory")
    public ResponseEntity<?> getActiveQuestionCountByCategory()
    {
        return categoryService.getActiveQuestionCountByCategory();
    }

    @Operation(summary = "Add a new category",description = "Adds a new category to the database.")
    @PostMapping("add")
    public ResponseEntity<String> addCategory(@RequestBody Category category)
    {
        return  categoryService.addCategory(category);
    }

    @Operation(summary = "Delete a category",description = "Deletes a category from the database using its ID.")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId)
    {
        return  categoryService.deleteCategory(categoryId);
    }

    @Operation(summary = "Update a category",description = "Updates the details of an existing category based on the provided ID.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable int categoryId,
            @RequestBody Category updatedCategory)
    {
        return  categoryService.updateCategory(categoryId,updatedCategory);
    }
}
