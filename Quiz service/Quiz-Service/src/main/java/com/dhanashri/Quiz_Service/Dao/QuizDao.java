package com.dhanashri.Quiz_Service.Dao;

import com.dhanashri.Quiz_Service.Module.Quiz;
import com.dhanashri.Quiz_Service.Module.QuizStatusCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface QuizDao extends JpaRepository<Quiz,Long> {

    @Query("SELECT q.isActive as isActive, COUNT(q) as count " +
            "FROM Quiz q " +
            "WHERE NOT EXISTS (SELECT 1 FROM Quiz q2 WHERE q2.previousVersionId = q.quiz_id) " +
            "GROUP BY q.isActive")
    List<QuizStatusCount> getQuizStatusCounts();

//    Page<Quiz> findByIsActive(boolean isactive, Pageable pageable);

    List<Quiz> findByPreviousVersionId(int previousVersionId);
    List<Quiz> findByIsActiveTrue();


    @Query("""
    SELECT q FROM Quiz q
    WHERE NOT EXISTS (
        SELECT 1 FROM Quiz q2
        WHERE q2.previousVersionId = q.quiz_id
    )
    AND q.isActive = :isActive
    ORDER BY q.createdAt DESC
    """)
    Page<Quiz> findLatestQuizzes(@Param("isActive") boolean isActive, Pageable pageable);

}
