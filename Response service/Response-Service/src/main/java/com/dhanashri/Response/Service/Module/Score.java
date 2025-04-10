package com.dhanashri.Response.Service.Module;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int score_id;

    private int user_id;
    private int quiz_id;
    private int score;
    private LocalDateTime created_at = LocalDateTime.now();

    public Score(int userId, int quizId, Integer score) {
        this.user_id = userId;
        this.quiz_id = quizId;
        this.score = score;
    }
}
