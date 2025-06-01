package com.dhanashri.Quiz_Service.Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDifficultyPair {
    private String categoryName;
    private String diffLevel;
    private int count;

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
