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
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String question;
    private String ans;
}
