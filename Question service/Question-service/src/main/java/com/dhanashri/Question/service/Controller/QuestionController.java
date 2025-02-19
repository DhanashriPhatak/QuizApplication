package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.QuestionWrapper;
import com.dhanashri.Question.service.Module.Response;
import com.dhanashri.Question.service.Service.QuestionService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question)
    {
        return questionService.addQuestion(question);
    }

    @PostMapping("edit")
    public ResponseEntity<String> editQuestion(@RequestBody Question question)
    {
        return questionService.editQuestion(question);
    }

    @DeleteMapping("deleteQuestion/{id}")
    public ResponseEntity<String> deleteQuesiton(@PathVariable int id)
    {
        return questionService.deleteQuestion(id);
    }

    @GetMapping("getQuestion/{id}")
    public ResponseEntity<QuestionWrapper> getQuesitonById(@PathVariable int id)
    {
        return questionService.getQuesitonById(id);
    }

    @GetMapping("getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }

    @GetMapping("getQuestionByCategory/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category)
    {
        return questionService.getQuestionsByCategory(category);
    }
    @GetMapping("generateQuiz")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam int numberOfQuestions)
    {
        return questionService.getQuestionsForQuiz(category,numberOfQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds)
    {
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }

}
