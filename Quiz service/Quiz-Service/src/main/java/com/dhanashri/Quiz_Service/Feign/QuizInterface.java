package com.dhanashri.Quiz_Service.Feign;


import com.dhanashri.Quiz_Service.Module.QuestionWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("QUESTION-SERVICE")
public interface QuizInterface {

    @GetMapping("question/generateQuiz")
    public ResponseEntity<List<Integer>> getQuestionForQuiz(@RequestParam String category,
                                                            @RequestParam int numberOfQuestions);

    @PostMapping("question/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionById(@RequestParam List<Integer> questionIds);



//    @PostMapping("question/getScore")
//    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
