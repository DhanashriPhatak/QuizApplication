package com.dhanashri.Question.service.Dao;

import com.dhanashri.Question.service.Module.Category;

import java.util.List;

public interface QuestionDaoCustom {
    List<Integer> findRandomQuestionIds(Category category, String diffLevel, int numberOfQuestions);
}
