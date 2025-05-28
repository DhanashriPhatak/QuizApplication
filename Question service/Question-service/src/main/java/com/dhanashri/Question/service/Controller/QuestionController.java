package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.Module.*;
import com.dhanashri.Question.service.Service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
@Tag(name = "Question API", description = "APIs to manage and retrieve quiz questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;


    @Operation(summary = "Add a new question", description = "Creates a new question and saves it to the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("add")
    public ResponseEntity<?> addQuestion(@RequestBody Question question)
    {
        return questionService.addQuestion(question);
    }

    @Operation(summary = "Update a question", description = "Updates the details of an existing question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @PostMapping("edit")
    public ResponseEntity<?> editQuestion(@RequestBody Question question)
    {
        return questionService.editQuestion(question);
    }

    @Operation(summary = "Delete question by ID", description = "Deletes a question based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuesiton(@PathVariable int id)
    {
        return questionService.deleteQuestion(id);
    }

    @Operation(summary = "Get question by ID", description = "Retrieves a single question by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question found"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("getQuestion/{id}")
    public ResponseEntity<QuestionWrapper> getQuesitonById(@PathVariable int id)
    {
        return questionService.getQuesitonById(id);
    }

    @Operation(summary = "Get all questions", description = "Retrieves all questions from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all questions retrieved")
    })
    @GetMapping("getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Get questions by category",
            description = "Fetches all questions associated with a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions by category retrieved")
    })
    @GetMapping("getQuestionByCategory/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable int category)
    {
        return questionService.getQuestionsByCategory(category);
    }

    @Operation(
            summary = "Generate a question for a quiz",
            description = "Fetches a list of question IDs from a specific category with the specified number of questions."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question IDs retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category ID or number of questions")
    })
    @GetMapping("generateQuiz")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam int categoryId,
                                                             @RequestParam int numberOfQuestions)
    {
        return questionService.getQuestionsForQuiz(categoryId,numberOfQuestions);
    }

    @Operation(
            summary = "Generate a list fo question for quiz created manually by choosing difficulty for each category",
            description = "Fetches a list of question IDs for a manual quiz based on category and difficulty."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question IDs for manual quiz retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters provided")
    })
    @PostMapping("generateQuizManual")
    public ResponseEntity<?> getQuestionsForManualQuiz(@RequestParam int categoryId,
                                                       @RequestParam String diffLevel,
                                                       @RequestParam int numberOfQuestions)
    {
        return questionService.getQuestionsForManualQuiz(categoryId,diffLevel,numberOfQuestions);
    }

    @Operation(
            summary = "Get full questions list",
            description = "Returns full question objects (wrapped) for a list of question IDs."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input question IDs")
    })
    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds)
    {
        return questionService.getQuestionsFromId(questionIds);
    }

    @Operation(
            summary = "Calculate quiz score",
            description = "Computes the score for a quiz based on the user's responses."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score calculated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid responses provided")
    })
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses)
    {
        return questionService.getScore(responses);
    }

    @Operation(
            summary = "Toggle question status",
            description = "Sets a question as active or inactive based on its current status."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question status toggled successfully"),
            @ApiResponse(responseCode = "404", description = "Question ID not found")
    })
    @PutMapping("toggle/{id}")
    public ResponseEntity<?> toggleQuestionStatus(@PathVariable int id)
    {
        return questionService.toggleQuestionStatus(id);
    }

}
