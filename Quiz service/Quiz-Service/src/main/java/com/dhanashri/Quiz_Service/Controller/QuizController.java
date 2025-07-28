package com.dhanashri.Quiz_Service.Controller;

import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizRequest;
import com.dhanashri.Quiz_Service.DTO.Request.QuizDTO;
import com.dhanashri.Quiz_Service.DTO.Response.QuestionUsageResponse;
import com.dhanashri.Quiz_Service.DTO.Response.QuizDetailResponse;
import com.dhanashri.Quiz_Service.Module.*;
import com.dhanashri.Quiz_Service.Service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Long> createQuiz(@RequestBody QuizDTO quizDTO)
    {
        return ResponseEntity.ok(quizService.createQuiz(quizDTO));
    }

    @Operation(summary = "Create a manual quiz",
            description = "Creates a quiz using a list of manually selected difficulty level for each category and no of question.")
    @PostMapping("generateQuizManual")
    public ResponseEntity<Long> generateQuizManual(@RequestBody ManualQuizRequest manualQuizRequest)
    {
        return ResponseEntity.ok(quizService.generateQuizManual(manualQuizRequest));
    }

    @Operation(summary = "Get quiz questions by quiz ID",
            description = "Retrieves all questions for the given quiz ID.")
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Long id)
    {
        return ResponseEntity.ok(quizService.getQuizQuestions(id));
    }

    @Operation(summary = "Get quiz questions for preview",
            description = "Fetches questions without answers to be previewed newly created quiz by Admin.")
    @GetMapping("getQuestionPreview/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestionsForPreview(
            @Parameter(description = "Quiz ID", required = true)
            @PathVariable Long id)
    {
        return ResponseEntity.ok(quizService.getQuizQuestionsForPreview(id));
    }

    @Operation(summary = "Get count of active and inactive quizzes",
            description = "Returns the count of active and inactive quizzes for to show on the quiz tabs.")
    @GetMapping("getActiveInactiveCount")
    public ResponseEntity<Map<String,Long>> getActiveInactiveCount()
    {
        return ResponseEntity.ok(quizService.getActiveInactiveCount());
    }

    @Operation(summary = "Get paginated list of quizzes",
            description = "Retrieves quizzes with pagination based on active/inactive status.")
    @GetMapping("quizList")
    public ResponseEntity<Page<Quiz>> getPaginatedQuizzes(@RequestParam boolean isActive, @RequestParam int page, @RequestParam int size)
    {
        return ResponseEntity.ok(quizService.getPaginatedQuizzes(isActive,page,size));
    }

    @Operation(summary = "Get quiz details by ID",
            description = "Fetches the complete quiz details including questions and metadata.")
    @GetMapping("view/{id}")
    public ResponseEntity<QuizDetailResponse> getQuizDetailsById(@PathVariable Long id)
    {
        return ResponseEntity.ok(quizService.getQuizDetailsById(id));
    }

    @Operation(summary = "Update auto mode quiz",
    description = "update the quiz details for auto mode quiz.")
    @PostMapping("update")
    public ResponseEntity<Long> updateQuiz(@RequestBody QuizDTO quizDTO)
    {
        return ResponseEntity.ok(quizService.updateQuiz(quizDTO));
    }


    @Operation(summary = "Update Manual mode quiz",
            description = "update the quiz details for Manual mode quiz.")
    @PostMapping("update/manual")
    public ResponseEntity<Long> updateManualQuiz(@RequestBody ManualQuizRequest manualQuizRequest)
    {
        return ResponseEntity.ok(quizService.updateManualQuiz(manualQuizRequest));
    }

    @Operation(summary = "Delete Quiz" ,
    description = "Delete a quiz by its id")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Long id)
    {
        quizService.deleteQuiz(id);
        return ResponseEntity.ok("Quiz Deleted Successfully");
    }

    @Operation(summary = "View Quiz History" ,
            description = "View quiz History by its id")
    @GetMapping("{quizId}/history")
    public ResponseEntity<List<Quiz>> getQuizHistory(@PathVariable Long quizId)
    {
        return ResponseEntity.ok(quizService.getQuizHistory(quizId));
    }

    @Operation(summary = "Activate quiz" ,
            description = "activate quiz from history and make the all other inactive in version lineage")
    @PutMapping("activate/{quizId}")
    public ResponseEntity<String> activateQuiz(@PathVariable Long quizId)
    {
        quizService.activateQuiz(quizId);
        return ResponseEntity.ok("Quiz Activated Successfully");
    }

    @Operation(summary = "deactivate quiz" ,
            description = "deactivate quiz from Active tab")
    @PutMapping("deactivate/{quizId}")
    public ResponseEntity<String> deactivateQuiz(@PathVariable Long quizId)
    {
        quizService.deactivateQuiz(quizId);
        return ResponseEntity.ok("Quiz Deactivated Successfully");
    }

    @Operation(summary = "Question is present in active quiz or not" ,
            description = "Check if give question id is present in any of the active quiz or not")
    @GetMapping("isQuestionUsed/{questionId}")
    public ResponseEntity<QuestionUsageResponse> isQuestionUsedInActiveQuiz(@PathVariable Long questionId)
    {
        return ResponseEntity.ok(quizService.isQuestionUsedInActiveQuiz(questionId));
    }
}
