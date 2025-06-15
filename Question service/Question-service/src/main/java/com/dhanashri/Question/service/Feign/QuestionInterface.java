package com.dhanashri.Question.service.Feign;

import com.dhanashri.Question.service.DTO.Response.QuestionUsageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="QUIZ-SERVICE")
public interface QuestionInterface {

    @GetMapping("quiz/isQuestionUsed/{questionId}")
    public ResponseEntity<QuestionUsageResponse> isQuestionUsedInActiveQuiz(@PathVariable Long questionId);
}
