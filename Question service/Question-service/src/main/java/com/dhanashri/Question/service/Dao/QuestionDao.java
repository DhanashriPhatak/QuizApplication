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
}
