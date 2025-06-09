package com.dhanashri.Quiz_Service.Controller;

import com.dhanashri.Quiz_Service.Module.*;
import com.dhanashri.Quiz_Service.Service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
@Tag(name="Quiz Controller" , description = "Handles quiz creation. listing and retrieval operation on quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Operation(summary = "Create a quiz based on category and difficulty",
            description = "Creates a new quiz with the provided title, category, and difficulty level.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quiz created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing fields")
    })
    @PostMapping("generateQuiz")
    public ResponseEntity<?> createQuiz(@RequestBody QuizDTO quizDTO)
    {
        return quizService.createQuiz(quizDTO);
    }

    @Operation(summary = "Create a manual quiz",
            description = "Creates a quiz using a list of manually selected difficulty level for each category and no of question.")
    @PostMapping("generateQuizManual")
    public ResponseEntity<?> generateQuizManual(@RequestBody ManualQuizRequest manualQuizRequest)
    {
        System.out.println("inside controller");
        return quizService.generateQuizManual(manualQuizRequest);
    }

    @Operation(summary = "Get quiz questions by quiz ID",
            description = "Retrieves all questions for the given quiz ID.")
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Long id)
    {
        return quizService.getQuizQuestions(id);
    }

    @Operation(summary = "Get quiz questions for preview",
            description = "Fetches questions without answers to be previewed newly created quiz by Admin.")
    @GetMapping("getQuestionPreview/{id}")
    public ResponseEntity<?> getQuizQuestionsForPreview(
            @Parameter(description = "Quiz ID", required = true)
            @PathVariable Long id)
    {
        return quizService.getQuizQuestionsForPreview(id);
    }

    @Operation(summary = "Get count of active and inactive quizzes",
            description = "Returns the count of active and inactive quizzes for to show on the quiz tabs.")
    @GetMapping("getActiveInactiveCount")
    public ResponseEntity<?> getActiveInactiveCount()
    {
        return quizService.getActiveInactiveCount();
    }

    @Operation(summary = "Get paginated list of quizzes",
            description = "Retrieves quizzes with pagination based on active/inactive status.")
    @GetMapping("quizList")
    public ResponseEntity<?> getPaginatedQuizzes(@RequestParam boolean isActive, @RequestParam int page,@RequestParam int size)
    {
        return quizService.getPaginatedQuizzes(isActive,page,size);
    }

    @Operation(summary = "Get quiz details by ID",
            description = "Fetches the complete quiz details including questions and metadata.")
    @GetMapping("view/{id}")
    public ResponseEntity<?> getQuizDetailsById(@PathVariable Long id)
    {
        return quizService.getQuizDetailsById(id);
    }

    @Operation(summary = "Update auto mode quiz",
    description = "update the quiz details for auto mode quiz.")
    @PostMapping("update")
    public ResponseEntity<?> updateQuiz(@RequestBody QuizDTO quizDTO)
    {
        return quizService.updateQuiz(quizDTO);
    }


    @Operation(summary = "Update Manual mode quiz",
            description = "update the quiz details for Manual mode quiz.")
    @PostMapping("update/manual")
    public ResponseEntity<?> updateManualQuiz(@RequestBody ManualQuizRequest manualQuizRequest)
    {
        return quizService.updateManualQuiz(manualQuizRequest);
    }

    @Operation(summary = "Delete Quiz" ,
    description = "Delete a quiz by its id")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id)
    {
        return quizService.deleteQuiz(id);
    }

}
