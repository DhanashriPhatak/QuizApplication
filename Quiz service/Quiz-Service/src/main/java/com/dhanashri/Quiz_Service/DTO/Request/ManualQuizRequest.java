package com.dhanashri.Quiz_Service.DTO.Request;

import com.dhanashri.Quiz_Service.DTO.Request.ManualQuizDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualQuizRequest {
    private Long quizId;
    private String quizTitle;
    private int totalQuestions;
    private List<ManualQuizDTO> configList;
    private String mode;//
}
