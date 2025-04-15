package com.dhanashri.Question.service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWrapper {
    private int id;
    private String question;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private Category category;


}
