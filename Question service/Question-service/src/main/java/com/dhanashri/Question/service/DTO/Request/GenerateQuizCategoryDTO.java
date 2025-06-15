package com.dhanashri.Question.service.DTO.Request;

public interface GenerateQuizCategoryDTO {
    Integer getCategoryId();
    String getCategory();
    Integer getTotal();
    Integer getEasyCount();
    Integer getMediumCount();
    Integer getHardCount();
}
