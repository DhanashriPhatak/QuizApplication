package com.dhanashri.Quiz_Service.Module;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchTransactionManager;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer quiz_question_id;

    @ManyToOne
    @JoinColumn(name="quiz_id")
    @JsonBackReference
    private Quiz quiz;

    private int question_id;

    //private int display_order;
    //private int points;
}
