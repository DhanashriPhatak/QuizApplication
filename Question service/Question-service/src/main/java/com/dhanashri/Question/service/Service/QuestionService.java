package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.DTO.Response.QuestionWrapper;
import com.dhanashri.Question.service.DTO.Response.QuestionUsageResponse;
import com.dhanashri.Question.service.Dao.CategoryDao;
import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Exception.ResourceNotFoundException;
import com.dhanashri.Question.service.Feign.QuestionInterface;
import com.dhanashri.Question.service.Module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    QuestionInterface questionInterface;

    //To Add a question in Question Database
    public Question addQuestion(Question question) {
        return questionDao.save(question);
    }

    public Question editQuestion(Question question) {
        return questionDao.save(question);
    }

    public void deleteQuestion(Long id) {
        if (!questionDao.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionDao.deleteById(id);
    }

    public QuestionWrapper getQuesitonById(Long id) {
        Question question = questionDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + id));
        QuestionWrapper questionWrapper = new QuestionWrapper();
        questionWrapper.setId(question.getId());
        questionWrapper.setQuestion(question.getQuestion());
        questionWrapper.setOption_1(question.getOption_a());
        questionWrapper.setOption_2(question.getOption_b());
        questionWrapper.setOption_3(question.getOption_c());
        questionWrapper.setOption_4(question.getOption_d());
        questionWrapper.setCategory(question.getCategoryName());

        return  questionWrapper;
    }

    public List<Question> getAllQuestions() {

        return questionDao.findAll();
    }

    public List<Question> getQuestionsByCategory(int categoryId) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        return questionDao.findByCategory(category);
    }

    public List<Long> getQuestionsForQuiz(int categoryId, int numberOfQuestions) {
        return questionDao.findRandomQuestionsByCategory(categoryId,numberOfQuestions);
    }

    public List<Long> getQuestionsForManualQuiz(int categoryId, String diffLevel,int numberOfQuestions) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        return questionDao.findRandomQuestionIds(category, diffLevel, numberOfQuestions);
    }

    public List<QuestionWrapper> getQuestionsFromId(List<Long> questionIds) {
        List<QuestionWrapper> questionWrapper = new ArrayList<>();

        for(Long id:questionIds)
        {
            Question q = questionDao.findById(id)
                    .orElseThrow(()->new ResourceNotFoundException("Question Not found with id:"+id ));
//                QuestionWrapper questionWrapper1 = new QuestionWrapper();
//                questionWrapper1.setId(q.getId());
//                questionWrapper1.setQuestion(q.getQuestion());
//                questionWrapper1.setCategory(q.getCategoryName());
//                questionWrapper1.setOption_1(q.getOption_a());
//                questionWrapper1.setOption_2(q.getOption_b());
//                questionWrapper1.setOption_3(q.getOption_c());
//                questionWrapper1.setOption_4(q.getOption_d());
//                questionWrapper1.setDiff_level(q.getDiff_level());

            questionWrapper.add(new QuestionWrapper(q));
        }
        return questionWrapper;
    }

    public int getScore(List<Response> responses) {

        int score = 0;
        for(Response response:responses)
        {
            Question question = questionDao.findById(response.getQuiz_question_id())
                    .orElseThrow(()->new ResourceNotFoundException("Question Not found with this id:-"+response.getQuiz_question_id()));
            if(response.getResponse().equals(question.getAns()))
            {
                score++;
            }
        }
        return score;
    }

    public Question toggleQuestionStatus(Long id) {

        Question question = questionDao.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Question not found with id: " + id));
        if(question.isActive())
        {
            ResponseEntity<?> res = questionInterface.isQuestionUsedInActiveQuiz(id);
            QuestionUsageResponse questionUsageResponse = (QuestionUsageResponse) res.getBody();
            if(res.getStatusCode() == HttpStatus.OK) {
                assert questionUsageResponse != null;
                if (questionUsageResponse.isUsed()) {
                    String message = "Cannot deactivate question. It's used in an active quiz."+
                            String.join(", ",questionUsageResponse.getQuizTitle());
                    throw new IllegalStateException(message);
                }
            }
        }
        question.setActive(!question.isActive());
        return questionDao.save(question);
    }

    public Map<String,Object> validateQuestions(List<Long> questionsIds) {
        List<Question> questionList = questionDao.findAllById(questionsIds);
        List<QuestionWrapper> inactiveQuestions = questionList.stream()
                .filter(q->!q.isActive())
                .map(QuestionWrapper::new)
                .toList();
        Map<String,Object> result =new HashMap<>();
        result.put("totalCount",questionsIds.size());
        result.put("activeCoubt",questionsIds.size()-inactiveQuestions.size());
        result.put("inactiveCoubt",inactiveQuestions.size());
        result.put("allActive",inactiveQuestions.isEmpty());
        result.put("inactiveQuestions",inactiveQuestions);

        return result;
    }
}
