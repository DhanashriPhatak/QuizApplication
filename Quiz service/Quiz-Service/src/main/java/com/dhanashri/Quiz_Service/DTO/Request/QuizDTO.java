package com.dhanashri.Quiz_Service.DTO.Request;

import lombok.Data;

import java.util.List;

@Data
public class QuizDTO {
    private Long quizId;
    private List<Integer> categoryId;
    private String quizTitle;
    private int numberOfQuestions;
    private String mode;
}
