package com.dhanashri.Question.service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUsageResponse {
    private boolean used;
    private List<String> quizTitle;
}
