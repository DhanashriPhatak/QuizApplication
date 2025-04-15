package com.dhanashri.Question.service.Module;

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
    private Category category;
    private String diff_level;
    private String question;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private String ans;
    private int isActive;

}
