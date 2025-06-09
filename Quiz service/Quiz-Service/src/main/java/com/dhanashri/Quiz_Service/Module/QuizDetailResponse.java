package com.dhanashri.Quiz_Service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDetailResponse {
    private Long quizId;
    private String quizTitle;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<QuestionWrapper> questionWrapperList;
    private List<CategoryDifficultyPair> categoryDifficultyPairList;
    private String mode;

}
