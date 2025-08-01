package com.dhanashri.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuizNotificationEvent {
    private long quizId;
    private String quizName;
    private LocalDateTime createdAt;
    private int numberOfQuestion;

    public QuizNotificationEvent(long quizId, String quizName, LocalDateTime createdAt, int numberOfQuestion) {
        this.quizId = quizId;
        this.quizName = quizName;
        this.createdAt = createdAt;
        this.numberOfQuestion = numberOfQuestion;
    }
}