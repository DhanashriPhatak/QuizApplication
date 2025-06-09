package com.dhanashri.Quiz_Service.Module;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quiz_id;
    private String quiz_title;

    @Column(name = "mode")
    private String mode;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<QuizQuestion> questions = new ArrayList<>();

    @Column(nullable = false)
    private boolean isActive=true;

    @Column(nullable = false)
    private Long version =1L;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="previous_version_id")
    private Long previousVersionId;

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions.clear();
        this.questions.addAll(questions);
    }
}
