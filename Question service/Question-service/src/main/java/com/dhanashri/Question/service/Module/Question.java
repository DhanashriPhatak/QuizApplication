package com.dhanashri.Question.service.Module;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String category;
    private String diff_level;
    private String question;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String ans;
    private int isActive;

    public Question(int id, String category, String diff_level, String question, String option_1, String option_2, String option_3, String option_4, String ans, int isActive) {
        this.id = id;
        this.category = category;
        this.diff_level = diff_level;
        this.question = question;
        this.option_1 = option_1;
        this.option_2 = option_2;
        this.option_3 = option_3;
        this.option_4 = option_4;
        this.ans = ans;
        this.isActive = isActive;
    }
}
