package com.dhanashri.Question.service.Module;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"questions"})
    private Category category;
    private String diff_level;
    private String question;
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    private String ans;
    private int isActive;

    @JsonProperty("category_id")
    public int getCategoryId() {
        return category != null ? category.getId() : -1;
    }

    @JsonProperty("category")
    public String getCategoryName() {
        return category != null ? category.getCategory() : null;
    }


}
