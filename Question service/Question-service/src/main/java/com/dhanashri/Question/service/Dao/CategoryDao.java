package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.CategoryStatsResponse;
import com.dhanashri.Question.service.Module.GenerateQuizCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {

    @Query(value = """
    SELECT 
        c.id as categoryId,
        c.category as category,
        COUNT(q.id) as total,
        COUNT(CASE WHEN q.diff_level = 'Easy' THEN 1 END) as easy,
        COUNT(CASE WHEN q.diff_level = 'Medium' THEN 1 END) as medium,
        COUNT(CASE WHEN q.diff_level = 'Hard' THEN 1 END) as hard,
        COUNT(CASE WHEN q.is_active = 1 THEN 1 END) as active,
        COUNT(CASE WHEN q.is_active = 0 THEN 1 END) as inactive
    FROM question q
    RIGHT JOIN category c ON q.category_id = c.id
    GROUP BY c.id, c.category
    ORDER BY c.id DESC
""", nativeQuery = true)
    List<CategoryStatsResponse> getCategoryStats();


    @Query(value = """
    SELECT
        c.id as categoryId,
        c.category as category,
        COUNT(CASE WHEN q.is_active = 1 THEN 1 END) as total,
        COUNT(CASE WHEN q.diff_level = 'Easy' AND q.is_active = 1 THEN 1 END) as easyCount,
        COUNT(CASE WHEN q.diff_level = 'Medium' AND q.is_active = 1 THEN 1 END) as mediumCount,
        COUNT(CASE WHEN q.diff_level = 'Hard' AND q.is_active = 1 THEN 1 END) as hardCount
    FROM question q
    RIGHT JOIN category c ON q.category_id = c.id
    GROUP BY c.id, c.category
    ORDER BY c.id
""", nativeQuery = true)
    List<GenerateQuizCategoryDTO> getGenerateQuizInventory();

}
