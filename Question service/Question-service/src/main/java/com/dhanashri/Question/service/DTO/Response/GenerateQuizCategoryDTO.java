package com.dhanashri.Question.service.DTO.Response;

public interface GenerateQuizCategoryDTO {
    Integer getCategoryId();
    String getCategory();
    Integer getTotal();
    Integer getEasyCount();
    Integer getMediumCount();
    Integer getHardCount();
}
