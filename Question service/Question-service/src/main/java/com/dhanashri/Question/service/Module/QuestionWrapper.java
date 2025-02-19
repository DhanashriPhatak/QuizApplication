package com.dhanashri.Question.service.Module;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuestionWrapper {
    private int id;
    private String question;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String category;

//    public QuestionWrapper() {
//    }

    public QuestionWrapper(int id, String option_1, String option_2, String option_3, String option_4, String question,String category) {
        this.id = id;
        this.option_1 = option_1;
        this.option_2 = option_2;
        this.option_3 = option_3;
        this.option_4 = option_4;
        this.question = question;
        this.category = category;
    }
}
