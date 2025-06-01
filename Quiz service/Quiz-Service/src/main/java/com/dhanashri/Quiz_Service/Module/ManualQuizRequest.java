package com.dhanashri.Quiz_Service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualQuizRequest {
    private String quizTitle;
    private List<ManualQuizDTO> configList;
    private String mode;//
}
