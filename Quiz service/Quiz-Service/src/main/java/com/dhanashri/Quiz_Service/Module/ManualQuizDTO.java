package com.dhanashri.Quiz_Service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualQuizDTO {
    private int categoryId;
    private String diffLevel;
    private int numberOfQuestions;

}
