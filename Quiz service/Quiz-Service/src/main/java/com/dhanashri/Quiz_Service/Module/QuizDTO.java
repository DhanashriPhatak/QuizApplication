package com.dhanashri.Quiz_Service.Module;

import lombok.Data;

@Data
public class QuizDTO {
    private int categoryId;
    private String quizTitle;
    private int numberOfQuestions;
}
