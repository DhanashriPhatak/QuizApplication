package com.dhanashri.Question.service.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryStatsResponse {
    private int categoryId;
    private String categoryName;
    private long totalQuestions;
    private long easyCount;
    private long mediumCount;
    private long hardCount;
    private long activeCount;
    private long inactiveCount;
}
