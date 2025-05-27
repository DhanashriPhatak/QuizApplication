package com.dhanashri.Quiz_Service.Module;

import java.time.LocalDateTime;
import java.util.List;

public class QuizDetailResponse {
    private int quizId;
    private String quizTitle;
    private LocalDateTime createdAt;
    private boolean isActive;
    private List<QuestionWrapper> questionWrapperList;
    private List<CategoryDifficultyPair> categoryDifficultyPairLsit;

    public QuizDetailResponse() {
    }

    public QuizDetailResponse(int quizId, String quizTitle, LocalDateTime createdAt, boolean isActive, List<QuestionWrapper> questionWrapperList, List<CategoryDifficultyPair> categoryDifficultyPairLsit) {
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.questionWrapperList = questionWrapperList;
        this.categoryDifficultyPairLsit = categoryDifficultyPairLsit;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<QuestionWrapper> getQuestionWrapperList() {
        return questionWrapperList;
    }

    public void setQuestionWrapperList(List<QuestionWrapper> questionWrapperList) {
        this.questionWrapperList = questionWrapperList;
    }

    public List<CategoryDifficultyPair> getCategoryDifficultyPairLsit() {
        return categoryDifficultyPairLsit;
    }

    public void setCategoryDifficultyPairLsit(List<CategoryDifficultyPair> categoryDifficultyPairLsit) {
        this.categoryDifficultyPairLsit = categoryDifficultyPairLsit;
    }


}
