package com.dhanashri.Quiz_Service.Service;

import com.dhanashri.Quiz_Service.Dao.QuizDao;
import com.dhanashri.Quiz_Service.Feign.QuizInterface;
import com.dhanashri.Quiz_Service.Module.QuestionWrapper;
import com.dhanashri.Quiz_Service.Module.Quiz;
import com.dhanashri.Quiz_Service.Module.QuizDTO;
import com.dhanashri.Quiz_Service.Module.QuizQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(QuizDTO quizDTO) {
        try{
            List<Integer> questionIds = quizInterface.getQuestionForQuiz(quizDTO.getCategory(), quizDTO.getNumberOfQuestions()).getBody();
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(quizDTO.getQuiz_title());
            List<QuizQuestion> quizQuestionList = new ArrayList<>();
            assert questionIds != null;
            for(int i:questionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuestion_id(i);
                quizQuestion.setQuiz(quiz);
                quizQuestionList.add(quizQuestion);
            }
            System.out.println("check");
            quiz.setQuestions(quizQuestionList);
            quizDao.save(quiz);

//            return new ResponseEntity<>("Quiz Created Successfully", HttpStatus.OK);
            return ResponseEntity.ok("Quiz Created Successfully");
        }
        catch(Exception e)
        {
//          return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
        try{

            Quiz quiz = quizDao.findById(id).get();
            List<QuizQuestion> quizQuestionList = quiz.getQuestions();
            List<Integer> questionIds = quizQuestionList.stream()
                    .map(QuizQuestion::getQuestion_id)
                    .collect(Collectors.toList());

            List<QuestionWrapper> questionWrapperList = quizInterface.getQuestionById(questionIds).getBody();

            return ResponseEntity.ok(questionWrapperList);
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }
}
