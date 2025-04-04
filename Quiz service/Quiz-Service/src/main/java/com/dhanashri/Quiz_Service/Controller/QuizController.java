package com.dhanashri.Quiz_Service.Controller;

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
    public ResponseEntity<String> createQuiz(@RequestBody QuizDTO quizDTO)
    {
        return quizService.createQuiz(quizDTO);
    }

    @PostMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable int id)
    {
        return quizService.getQuizQuestions(id);
    }
}
