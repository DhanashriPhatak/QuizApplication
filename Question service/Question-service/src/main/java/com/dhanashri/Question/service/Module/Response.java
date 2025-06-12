package com.dhanashri.Question.service.Module;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response {
    private Long quiz_question_id;
    private String response;
}
