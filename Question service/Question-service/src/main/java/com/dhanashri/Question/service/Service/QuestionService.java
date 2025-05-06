package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.Dao.CategoryDao;
import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.QuestionWrapper;
import com.dhanashri.Question.service.Module.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    CategoryDao categoryDao;

    //To Add a question in Question Database
    public ResponseEntity<String> addQuestion(Question question) {
        try{
            questionDao.save(question);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error Occurred while saving the data",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> editQuestion(Question question) {
        try{
            Question question1 = questionDao.save(question);
            return new ResponseEntity<>(question1,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error While updating the question",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteQuestion(int id) {
        try{
            questionDao.deleteById(id);
            return new ResponseEntity<>("Success",HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Error while deleting a question",HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<QuestionWrapper> getQuesitonById(int id) {
        QuestionWrapper questionWrapper = new QuestionWrapper();
        try{
            Optional<Question> questionOptional = questionDao.findById(id);
            Question question = questionOptional.orElseThrow(() -> new RuntimeException("Question not found"));

            questionWrapper.setId(question.getId());
            questionWrapper.setQuestion(question.getQuestion());
            questionWrapper.setOption_1(question.getOption_a());
            questionWrapper.setOption_2(question.getOption_b());
            questionWrapper.setOption_3(question.getOption_c());
            questionWrapper.setOption_4(question.getOption_d());
            questionWrapper.setCategory(question.getCategory());

            return new ResponseEntity<>(questionWrapper,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(questionWrapper,HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<List<Question>> getAllQuestions() {
        try{
            return new ResponseEntity<>(questionDao.findAll(),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(int category) {
        try{
            Category category1 = categoryDao.findById(category).get();
            return new ResponseEntity<>(questionDao.findByCategory(category1),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(int category, int numberOfQuestions) {
        try {
            Category category1 = categoryDao.findById(category).get();
            List<Integer> questionList = questionDao.findRandomQuestionsByCategory(category1,numberOfQuestions);

            return new ResponseEntity<>(questionList, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        try{
            List<QuestionWrapper> questionWrapper = new ArrayList<>();
            List<Question>  questions  = new ArrayList<>();
            for(int id:questionIds)
            {
                questions.add(questionDao.findById(id).get());
            }

            for(Question  q:questions)
            {
                QuestionWrapper questionWrapper1 = new QuestionWrapper();
                questionWrapper1.setId(q.getId());
                questionWrapper1.setQuestion(q.getQuestion());
                questionWrapper1.setCategory(q.getCategory());
                questionWrapper1.setOption_1(q.getOption_a());
                questionWrapper1.setOption_2(q.getOption_b());
                questionWrapper1.setOption_3(q.getOption_c());
                questionWrapper1.setOption_4(q.getOption_d());

                questionWrapper.add(questionWrapper1);
            }
            return new ResponseEntity<>(questionWrapper, HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        try{
            int score = 0;
            for(Response response:responses)
            {
                Question  question = questionDao.findById(response.getQuiz_question_id()).get();
                if(response.getResponse().equals(question.getAns()))
                {
                    score++;
                }
            }
            return new ResponseEntity<>(score,HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(0,HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> toggleQuestionStatus(int id) {
        try{
            Optional<Question> optionalQuestion = questionDao.findById(id);
            if(optionalQuestion.isPresent())
            {
                Question question = optionalQuestion.get();
                Integer currentStatus = Optional.of(question.getIsActive()).orElse(0);
                question.setIsActive(currentStatus == 0 ? 1 : 0);
//                question.setIsActive(question.getIsActive()==0?1:0);
                questionDao.save(question);
                return  new ResponseEntity<>(question,HttpStatus.OK);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
//            logger.error("Error while toggling question status", e);
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Failed to update the Question Status",HttpStatus.BAD_REQUEST);
        }
    }
}
