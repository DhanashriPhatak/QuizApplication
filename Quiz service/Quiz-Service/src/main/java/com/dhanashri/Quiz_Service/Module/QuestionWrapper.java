package com.dhanashri.Quiz_Service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWrapper {
    private Long id;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String question;
    private String category;
    private String diff_level;
}
