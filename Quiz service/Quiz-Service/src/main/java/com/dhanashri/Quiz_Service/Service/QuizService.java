package com.dhanashri.Quiz_Service.Service;

import com.dhanashri.Quiz_Service.Dao.QuizDao;
import com.dhanashri.Quiz_Service.Feign.QuizInterface;
import com.dhanashri.Quiz_Service.Module.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public ResponseEntity<?> createQuiz(QuizDTO quizDTO) {
        try{
            System.out.println("reached here:-");
            List<Integer> questionIds = quizInterface.getQuestionForQuiz(quizDTO.getCategoryId(),
                    quizDTO.getNumberOfQuestions()).getBody();
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(quizDTO.getQuizTitle());
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
            return new ResponseEntity<>(quiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
//          return new ResponseEntity<>("Something Went Wrong",HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> generateQuizManual(ManualQuizRequest manualQuizRequest) {
        try{
            List<Integer> allQuestionIds = new ArrayList<>();
            for(ManualQuizDTO manualQuizDTO:manualQuizRequest.getConfigList())
            {
                ResponseEntity<?> idsResponse  = quizInterface.getQuestionsForManualQuiz(manualQuizDTO.getCategoryId(),
                        manualQuizDTO.getDiff_level(),manualQuizDTO.getNumberOfQuestions());

                if(idsResponse .getStatusCode().is2xxSuccessful())
                {
                    List<Integer> ids = (List<Integer>) idsResponse .getBody();
                    if (ids != null) {
                        allQuestionIds.addAll(ids);
                    }
                }
                else {
                    return new ResponseEntity<>("Failed to fetch questions for Manual quiz", HttpStatus.BAD_REQUEST);
                }
            }
            Quiz quiz = new Quiz();
            quiz.setQuiz_title(manualQuizRequest.getQuizTitle());
            List<QuizQuestion> quizQuestionList = new ArrayList<>();

            for(int id:allQuestionIds)
            {
                QuizQuestion quizQuestion = new QuizQuestion();
                quizQuestion.setQuiz_question_id(id);
                quizQuestion.setQuiz(quiz);
                quizQuestionList.add(quizQuestion);
            }
            quiz.setQuestions(quizQuestionList);
            quizDao.save(quiz);

            return new ResponseEntity<>(quiz.getQuiz_id(),HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to Generate a Manual quiz", HttpStatus.BAD_REQUEST);
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
