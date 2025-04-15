package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category,Integer> {
}
