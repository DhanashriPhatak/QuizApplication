package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class QuestionDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionDao questionDao;

    @Test
    @DisplayName("Test findByCategory")
    void testFindByCategory() {
        Category category = new Category();
        category.setCategory("Programming");
        category = entityManager.persistAndFlush(category);

        Question q = new Question();
        q.setQuestion("What is Java?");
        q.setCategory(category);
        q.setOption_a("OOP");
        q.setOption_b("Database");
        q.setOption_c("Framework");
        q.setOption_d("OS");
        q.setAns("OOP");
        q.setDiff_level("easy");
        q.setActive(true);
        entityManager.persistAndFlush(q);

        List<Question> result = questionDao.findByCategory(category);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuestion()).isEqualTo("What is Java?");
    }

    @Test
    @DisplayName("Test findRandomQuestionsByCategory JPQL method")
    void testFindRandomQuestionsByCategoryQuery() {
        Category category = new Category();
        category.setCategory("Programming");
        category = entityManager.persistAndFlush(category);
        for (int i = 1; i <= 5; i++) {
            Question q = new Question();
            q.setQuestion("Random Q" + i);
            q.setCategory(category);
            q.setOption_a("A");
            q.setOption_b("B");
            q.setOption_c("C");
            q.setOption_d("D");
            q.setAns("A");
            q.setDiff_level("easy");
            q.setActive(true);
            entityManager.persist(q);
        }

        entityManager.flush();

        List<Long> result = questionDao.findRandomQuestionsByCategory(category.getId(), 3);
        assertThat(result).hasSize(3);
    }
}
