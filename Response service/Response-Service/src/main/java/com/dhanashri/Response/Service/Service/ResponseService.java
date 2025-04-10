package com.dhanashri.Response.Service.Service;

import com.dhanashri.Response.Service.Dao.ResponseDao;
import com.dhanashri.Response.Service.Dao.ScoreDao;
import com.dhanashri.Response.Service.Feign.ResponseInterface;
import com.dhanashri.Response.Service.Module.Response;
import com.dhanashri.Response.Service.Module.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseService {

    @Autowired
    ResponseInterface responseInterface;

    @Autowired
    ResponseDao responseDao;

    @Autowired
    ScoreDao scoreDao;

    public ResponseEntity<String> getScore(List<Response> response) {
        try{
            responseDao.saveAll(response);
            List<Response> questionResponses = convertToQuestionResponses(response);
            ResponseEntity<Integer> scoreResponse = responseInterface.getScore(questionResponses);
            Integer score = scoreResponse.getBody();
            if (score == null) {
                return ResponseEntity.badRequest().body("Failed to calculate score: null response");
            }
            Score score1= new Score(response.get(0).getUser_id(),response.get(0).getQuiz_id(),score);
            scoreDao.save(score1);
            return ResponseEntity.ok(String.valueOf(score));
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body("Something went wrong");
        }
    }

    private List<Response> convertToQuestionResponses(List<Response> response) {
        return response.stream()
                .map(response1 -> new Response(response1.getQuiz_question_id(), response1.getResponse()))
                .collect(Collectors.toList());
    }
}
