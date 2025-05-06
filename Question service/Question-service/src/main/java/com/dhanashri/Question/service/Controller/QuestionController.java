package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.QuestionWrapper;
import com.dhanashri.Question.service.Module.Response;
import com.dhanashri.Question.service.Service.QuestionService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    /*
    To Add a new Question
     */
    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question)
    {
        return questionService.addQuestion(question);
    }

    /*
    To edit any existing question
     */
    @PostMapping("edit")
    public ResponseEntity<?> editQuestion(@RequestBody Question question)
    {
        return questionService.editQuestion(question);
    }

    /*
    Description: To delete any question
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuesiton(@PathVariable int id)
    {
        return questionService.deleteQuestion(id);
    }

    /*
    Description:- To get a Single Question by ID
     */
    @GetMapping("getQuestion/{id}")
    public ResponseEntity<QuestionWrapper> getQuesitonById(@PathVariable int id)
    {
        return questionService.getQuesitonById(id);
    }

    /*
    Description:- To get All Questions
     */

    @GetMapping("getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }



    /*
    Description:- To get Questions by category
     */
    @GetMapping("getQuestionByCategory/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable int category)
    {
        return questionService.getQuestionsByCategory(category);
    }

    /*
    Description:- Generate Quiz by Category and No of questions
     */
    @GetMapping("generateQuiz")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam int category_id, @RequestParam int numberOfQuestions)
    {
        return questionService.getQuestionsForQuiz(category_id,numberOfQuestions);
    }

    /*
     Description: Return All the question based on list of id's provided
     */
    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds)
    {
        return questionService.getQuestionsFromId(questionIds);
    }

    /*
    Description:- Return Score got in the quiz based on response provided
     */
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }

    /**This is to set Questions Active / Inactive */
    @PutMapping("toggle/{id}")
    public ResponseEntity<?> toggleQuestionStatus(@PathVariable int id)
    {
        return questionService.toggleQuestionStatus(id);
    }

}
