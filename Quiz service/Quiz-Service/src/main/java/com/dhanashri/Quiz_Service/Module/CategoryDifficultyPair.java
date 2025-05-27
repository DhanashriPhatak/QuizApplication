package com.dhanashri.Quiz_Service.Module;

import java.util.Objects;

public class CategoryDifficultyPair {
    private String categoryName;
    private String diffLevel;
    private int count;

    public CategoryDifficultyPair() {
    }

    public CategoryDifficultyPair(String categoryName, String diffLevel,int count) {
        this.categoryName = categoryName;
        this.diffLevel = diffLevel;
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDiffLevel() {
        return diffLevel;
    }

    public void setDiffLevel(String diffLevel) {
        this.diffLevel = diffLevel;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDifficultyPair that = (CategoryDifficultyPair) o;
        return count == that.count && Objects.equals(categoryName, that.categoryName) && Objects.equals(diffLevel, that.diffLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryName, diffLevel, count);
    }
}
