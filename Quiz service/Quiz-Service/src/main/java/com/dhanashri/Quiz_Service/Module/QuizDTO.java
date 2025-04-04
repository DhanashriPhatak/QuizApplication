package com.dhanashri.Quiz_Service.Module;

import lombok.Data;

@Data
public class QuizDTO {
    private String category;
    private String quiz_title;
    private int numberOfQuestions;
}
