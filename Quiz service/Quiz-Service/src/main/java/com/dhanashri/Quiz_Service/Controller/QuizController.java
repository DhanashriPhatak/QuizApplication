package com.dhanashri.Quiz_Service.Controller;

import com.dhanashri.Quiz_Service.Module.ManualQuizDTO;
import com.dhanashri.Quiz_Service.Module.ManualQuizRequest;
import com.dhanashri.Quiz_Service.Module.QuestionWrapper;
import com.dhanashri.Quiz_Service.Module.QuizDTO;
import com.dhanashri.Quiz_Service.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("generateQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody QuizDTO quizDTO)
    {
        return quizService.createQuiz(quizDTO);
    }

    @PostMapping("generateQuizManual")
    public ResponseEntity<?> generateQuizManual(@RequestBody ManualQuizRequest manualQuizRequest)
    {
        System.out.println("inside controller");
        return quizService.generateQuizManual(manualQuizRequest);
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable int id)
    {
        return quizService.getQuizQuestions(id);
    }

    @GetMapping("getQuestionPreview/{id}")
    public ResponseEntity<?> getQuizQuestionsForPreview(@PathVariable int id)
    {
        return quizService.getQuizQuestionsForPreview(id);
    }
}
