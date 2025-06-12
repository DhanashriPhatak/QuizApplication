package com.dhanashri.Question.service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWrapper {
    private Long id;
    private String question;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String category;
    private String diff_level;

    public QuestionWrapper(Question question) {
        this.id = question.getId();
        this.diff_level = question.getDiff_level();
        this.question = question.getQuestion();
        this.option_1 = question.getOption_a();
        this.option_2 = question.getOption_b();
        this.option_3 = question.getOption_c();
        this.option_4 = question.getOption_d();
        this.category = question.getCategoryName();

    }
}
