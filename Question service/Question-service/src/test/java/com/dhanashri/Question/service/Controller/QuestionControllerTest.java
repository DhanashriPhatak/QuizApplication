package com.dhanashri.Question.service.Controller;

import com.dhanashri.Question.service.DTO.Response.QuestionWrapper;
import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.Response;
import com.dhanashri.Question.service.Service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import jakarta.validation.Validator;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = QuestionController.class)
public class QuestionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Question sampleQuestion;

    @TestConfiguration
    static class MockConfig {


        @Bean
        public Validator validator() {
            return new LocalValidatorFactoryBean();
        }
    }

    @BeforeEach
    void setUp() {
        sampleQuestion = new Question();
        sampleQuestion.setId(1L);
        sampleQuestion.setQuestion("What is Java?");
        sampleQuestion.setOption_a("OOP");
        sampleQuestion.setOption_b("Database");
        sampleQuestion.setOption_c("Framework");
        sampleQuestion.setOption_d("OS");
        sampleQuestion.setAns("OOP");
        sampleQuestion.setDiff_level("easy");
        sampleQuestion.setActive(true);

        Category category = new Category();
        category.setId(101);
        category.setCategory("Programming");
        sampleQuestion.setCategory(category);
    }

    @Test
    void testAddQuestion_Success() throws Exception {
        // Setup nested category
        Map<String, Object> category = new HashMap<>();
        category.put("id", 101);
        category.put("category", "Programming");

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1L);
        requestBody.put("question", "What is Java?");
        requestBody.put("option_a", "OOP");
        requestBody.put("option_b", "Database");
        requestBody.put("option_c", "Framework");
        requestBody.put("option_d", "OS");
        requestBody.put("ans", "OOP");
        requestBody.put("diff_level", "easy");
        requestBody.put("active", true);
        requestBody.put("category", category);

        // Mock service response
        when(questionService.addQuestion(any(Question.class))).thenReturn(sampleQuestion);

        // Perform request and validate
        mockMvc.perform(post("/question/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleQuestion.getId()))
                .andExpect(jsonPath("$.question").value("What is Java?"));
    }

    @Test
    void testEditQuestion() throws Exception{
        sampleQuestion.setQuestion("Updated Question");

        // Prepare nested Category map for request JSON
        Map<String, Object> category = new HashMap<>();
        category.put("id", 101);
        category.put("category", "Programming");

        // Prepare request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 1L);
        requestBody.put("question", "Updated Question");
        requestBody.put("option_a", "OOP");
        requestBody.put("option_b", "Database");
        requestBody.put("option_c", "Framework");
        requestBody.put("option_d", "OS");
        requestBody.put("ans", "OOP");
        requestBody.put("diff_level", "easy");
        requestBody.put("active", true);
        requestBody.put("category", category);

        // Mocking the service call
        when(questionService.editQuestion(any(Question.class))).thenReturn(sampleQuestion);

        // Execute and verify
        mockMvc.perform(post("/question/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Updated Question"));
    }

    @Test
    void testDeleteQuestion_Success() throws Exception {
        // No return expected, just verify it doesn't throw
        mockMvc.perform(delete("/question/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Question deleted successfully"));
    }

    @Test
    void testGetQuestionById_Success() throws Exception {
        QuestionWrapper wrapper = new QuestionWrapper();
        wrapper.setId(sampleQuestion.getId());
        wrapper.setQuestion(sampleQuestion.getQuestion());
        wrapper.setOption_1(sampleQuestion.getOption_a());
        wrapper.setOption_2(sampleQuestion.getOption_b());
        wrapper.setOption_3(sampleQuestion.getOption_c());
        wrapper.setOption_4(sampleQuestion.getOption_d());
        wrapper.setCategory(sampleQuestion.getCategoryName());

        when(questionService.getQuesitonById(1L)).thenReturn(wrapper);

        mockMvc.perform(get("/question/getQuestion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.question").value("What is Java?"))
                .andExpect(jsonPath("$.category").value("Programming"));
    }

    @Test
    void testGetAllQuestions_Success() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(List.of(sampleQuestion));

        mockMvc.perform(get("/question/getAllQuestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].question").value("What is Java?"));
    }

    @Test
    void testGetQuestionsByCategory_Success() throws Exception {
        int categoryId = 101;

        when(questionService.getQuestionsByCategory(categoryId)).thenReturn(List.of(sampleQuestion));

        mockMvc.perform(get("/question/getQuestionByCategory/{category}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(sampleQuestion.getId()))
                .andExpect(jsonPath("$[0].question").value("What is Java?"));
    }

    @Test
    void testGetQuestionsForQuiz_Success() throws Exception{
        int categoryId = 101;
        int numberOfQuestions = 3;
        List<Long> mockIds = List.of(10L,11L,12L);

        when(questionService.getQuestionsForQuiz(categoryId,numberOfQuestions)).thenReturn(mockIds);

        mockMvc.perform(get("/question/generateQuiz")
                .param("categoryId",String.valueOf(categoryId))
                .param("numberOfQuestions",String.valueOf(numberOfQuestions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0]").value(10))
                .andExpect(jsonPath("$[1]").value(11))
                .andExpect(jsonPath("$[2]").value(12));
    }

    @Test
    void testGetQuestionsForManualQuiz_Success() throws Exception{
        int categoryId = 101;
        String diffLevel = "easy";
        int numberOfQuestions = 2;
        List<Long> mockQuestionIds = List.of(100L, 101L);

        when(questionService.getQuestionsForManualQuiz(categoryId,diffLevel,numberOfQuestions)).thenReturn(mockQuestionIds);

        mockMvc.perform(post("/question/generateQuizManual")
                .param("categoryId",String.valueOf(categoryId))
                .param("diffLevel",String.valueOf(diffLevel))
                .param("numberOfQuestions",String.valueOf(numberOfQuestions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0]").value(100))
                .andExpect(jsonPath("$[1]").value(101));
    }

    @Test
    void testGetQuestionsFromId_Success()throws Exception{
        List<Long> questionIds = List.of(1L, 2L);
        QuestionWrapper qw1 = new QuestionWrapper();
        qw1.setId(1L);
        qw1.setQuestion("What is Java?");
        qw1.setOption_1("OOP");
        qw1.setOption_2("DB");
        qw1.setOption_3("Framework");
        qw1.setOption_4("OS");
        qw1.setCategory("Programming");

        QuestionWrapper qw2 = new QuestionWrapper();
        qw2.setId(2L);
        qw2.setQuestion("What is Spring?");
        qw2.setOption_1("Library");
        qw2.setOption_2("Framework");
        qw2.setOption_3("IDE");
        qw2.setOption_4("Tool");
        qw2.setCategory("Java");

        when(questionService.getQuestionsFromId(questionIds)).thenReturn(List.of(qw1,qw2));

        mockMvc.perform(post("/question/getQuestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question").value("What is Java?"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].question").value("What is Spring?"));
    }

    @Test
    void testGetScore_Success()throws Exception{
        List<Response> mockResponses = new ArrayList<>();
        Response r1 = new Response();
        r1.setQuiz_question_id(1L);
        r1.setResponse("A");
        mockResponses.add(r1);

        Response r2 = new Response();
        r2.setQuiz_question_id(2L);
        r2.setResponse("B");
        mockResponses.add(r2);

        int expectedScore = 1;

        when(questionService.getScore(mockResponses)).thenReturn(expectedScore);

        mockMvc.perform(post("/question/getScore")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockResponses)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedScore)));
    }

    @Test
    void testToggleQuestionStatus_Success() throws Exception {
        sampleQuestion.setActive(false); // simulate toggled state
        when(questionService.toggleQuestionStatus(1L)).thenReturn(sampleQuestion);

        mockMvc.perform(put("/question/toggle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleQuestion.getId()))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void testValidateQuestions_Success() throws Exception {
        List<Long> questionIds = List.of(1L, 2L);
        Map<String, Object> mockResult = new HashMap<>();
        mockResult.put("totalCount", 2);
        mockResult.put("activeCoubt", 2);
        mockResult.put("inactiveCoubt", 0);
        mockResult.put("allActive", true);
        mockResult.put("inactiveQuestions", List.of());

        when(questionService.validateQuestions(questionIds)).thenReturn(mockResult);

        mockMvc.perform(post("/question/validateQuestions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(2))
                .andExpect(jsonPath("$.allActive").value(true));
    }

}
