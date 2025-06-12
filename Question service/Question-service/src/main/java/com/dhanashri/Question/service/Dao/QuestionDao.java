package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Long>,QuestionDaoCustom{

    List<Question> findByCategory(Category category);

    @Query(value = "select q.id from question q where q.category_id=:categoryId and q.is_active = 1 order by RANDOM() LIMIT :numQ",nativeQuery = true)
    List<Long> findRandomQuestionsByCategory(int categoryId, int numQ);

//    @Query(value= """
//            SELECT q.id from quesiton q
//            WHERE q.category = ?1 AND q.diff_level = ?2 ANDq.is_active=1
//            ORDER BY RANDOM()
//            LIMIT ?3
//            """,nativeQuery = true)
//    List<Integer> findRandomByCategoryAndDiffLevel(int category,String diffLevel,int numberOfQuestions);
}
