package com.dhanashri.Quiz_Service.Exception;

import java.util.List;
import java.util.Map;

public class InvalidQuizActivationException extends RuntimeException {
    private final List<Map<String,Object>> inactiveQuestions;

    public InvalidQuizActivationException(String message, List<Map<String,Object>> inactiveQuestions) {
        super(message);
        this.inactiveQuestions = inactiveQuestions;
    }

    public List<Map<String,Object>> getInactiveQuestionIds() {
        return inactiveQuestions;
    }
}
