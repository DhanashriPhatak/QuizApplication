package com.dhanashri.Question.service.Dao;


import com.dhanashri.Question.service.Module.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionDaoImpl implements  QuestionDaoCustom{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Long> findRandomQuestionIds(Category category, String diffLevel, int numberOfQuestions) {
        String sql = """
            SELECT q.id FROM question q
            WHERE q.category_id = :category
              AND q.diff_level = :diffLevel
              AND q.is_active = 1
            ORDER BY RANDOM()
            LIMIT :numberOfQuestions
        """;

        List<Long> resultList = entityManager.createNativeQuery(sql)
                .setParameter("category", category.getId())
                .setParameter("diffLevel", diffLevel)
                .setParameter("numberOfQuestions", numberOfQuestions)
                .getResultList();

        return resultList;
    }
}
