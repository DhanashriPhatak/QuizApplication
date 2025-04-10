package com.dhanashri.Response.Service.Module;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int response_id;

    private int user_id;
    private int quiz_id;
    private int quiz_question_id;
    private String response;

    private LocalDateTime created_at = LocalDateTime.now();

    public Response(int quiz_question_id, String response) {
        this.quiz_question_id = quiz_question_id;
        this.response = response;
    }

}
