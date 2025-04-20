package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.CategoryStatsResponse;
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
    JOIN category c ON q.category_id = c.id
    GROUP BY c.id, c.category
""", nativeQuery = true)
    List<CategoryStatsResponse> getCategoryStats();
}
