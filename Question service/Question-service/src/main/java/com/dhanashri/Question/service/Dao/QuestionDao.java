package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {

    List<Question> findByCategory(Category category);

    @Query(value = "select q.id from question q where q.category=:category order by RANDOM() LIMIT :numQ",nativeQuery = true)
    List<Integer> findRandomQuestionsByCategory(Category category, int numQ);

    @Query(value= """
            SELECT q.id from quesiton q
            WHERE q.category = :category AND q.diff_level = :diffLevel ANDq.is_active=1
            ORDER BY RAND()
            LIMIT :numberOfQuesitons
            """,nativeQuery = true)
    List<Integer> findRandomByCategoryAndDiffLevel(Category category,String diffLevel,int numberOfQuestions);
}
