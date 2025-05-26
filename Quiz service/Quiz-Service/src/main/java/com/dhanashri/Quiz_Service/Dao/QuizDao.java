package com.dhanashri.Quiz_Service.Dao;

import com.dhanashri.Quiz_Service.Module.Quiz;
import com.dhanashri.Quiz_Service.Module.QuizStatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface QuizDao extends JpaRepository<Quiz,Integer> {

    @Query(value = "SELECT is_active AS isActive, COUNT(*) AS count FROM quiz GROUP BY is_active",
            nativeQuery = true)
    List<QuizStatusCount> getQuizStatusCounts();

    Page<Quiz> findByIsActive(boolean isactive, Pageable pageable);
}
