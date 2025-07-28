package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuestionDaoImplTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private QuestionDao questionDao; // QuestionDao extends QuestionDaoCustom

    private Category category;

    @BeforeEach
    void setUp() {
        // Create and persist category
        category = new Category();
        category.setCategory("Programming");
        testEntityManager.persist(category);

        // Add multiple questions with same category & difficulty
        for (int i = 1; i <= 5; i++) {
            Question q = new Question();
            q.setQuestion("Q" + i);
            q.setOption_a("A");
            q.setOption_b("B");
            q.setOption_c("C");
            q.setOption_d("D");
            q.setAns("A");
            q.setActive(true);
            q.setDiff_level("easy");
            q.setCategory(category);
            testEntityManager.persist(q);
        }

        testEntityManager.flush();
    }

    @Test
    void testFindRandomQuestionIds() {
        List<Long> result = questionDao.findRandomQuestionIds(category, "easy", 3);

        // Validate size and contents
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
    }
}
