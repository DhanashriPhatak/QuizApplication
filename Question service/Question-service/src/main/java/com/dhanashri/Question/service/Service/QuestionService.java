package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.QuestionWrapper;
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

    public ResponseEntity<String> editQuestion(Question question) {
        try{
            questionDao.save(question);
            return new ResponseEntity<>("Success",HttpStatus.OK);
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
            Question question = questionOptional.get();

            questionWrapper.setId(question.getId());
            questionWrapper.setQuestion(question.getQuestion());
            questionWrapper.setOption_1(question.getOption_1());
            questionWrapper.setOption_2(question.getOption_2());
            questionWrapper.setOption_3(question.getOption_3());
            questionWrapper.setOption_4(question.getOption_4());
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

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try{
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }
}
