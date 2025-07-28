package com.dhanashri.Question.service.Module;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotBlank(message = "Category name must be specified")
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"questions"})
    private Category category;
    @NotBlank(message = "Difficulty level must be specified")
    private String diff_level;
    @NotBlank(message = "Question text must not be empty")
    private String question;
    @NotBlank(message = "Option A is required")
    private String option_a;
    @NotBlank(message = "Option B is required")
    private String option_b;
    @NotBlank(message = "Option C is required")
    private String option_c;
    @NotBlank(message = "Option D is required")
    private String option_d;
    @NotBlank(message = "Answer must not be empty")
    private String ans;
    private boolean isActive;

    @JsonProperty("category_id")
    public int getCategoryId() {
        return category != null ? category.getId() : -1;
    }

    @JsonProperty("category")
    public String getCategoryName() {
        return category != null ? category.getCategory() : null;
    }


}
