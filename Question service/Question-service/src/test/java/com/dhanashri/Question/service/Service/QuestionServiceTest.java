package com.dhanashri.Question.service.Service;

import com.dhanashri.Question.service.DTO.Response.QuestionUsageResponse;
import com.dhanashri.Question.service.DTO.Response.QuestionWrapper;
import com.dhanashri.Question.service.Dao.CategoryDao;
import com.dhanashri.Question.service.Dao.QuestionDao;
import com.dhanashri.Question.service.Dao.QuestionDaoCustom;
import com.dhanashri.Question.service.Exception.ResourceNotFoundException;
import com.dhanashri.Question.service.Feign.QuestionInterface;
import com.dhanashri.Question.service.Module.Category;
import com.dhanashri.Question.service.Module.Question;
import com.dhanashri.Question.service.Module.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    QuestionDao questionDao;

    @Mock
    private QuestionDaoCustom questionDaoCustom;

    @InjectMocks
    private QuestionService questionService;

    private Question sampleQuestion;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private QuestionInterface questionInterface;


    @BeforeEach
    void setUp()
    {
        sampleQuestion = new Question();
        Category category = new Category();
        category.setId(101);
        category.setCategory("Programming");
        sampleQuestion.setId(1L);
        sampleQuestion.setQuestion("What is Java?");
        sampleQuestion.setOption_a("OOP language");
        sampleQuestion.setOption_b("Database");
        sampleQuestion.setOption_c("Framework");
        sampleQuestion.setOption_d("Operating System");
        sampleQuestion.setAns("OOP language");
        sampleQuestion.setDiff_level("easy");
        sampleQuestion.setCategory(category);
        sampleQuestion.setActive(true);


    }

    @Test
    void testAddQuestion()
    {
        when(questionDao.save(sampleQuestion)).thenReturn(sampleQuestion);
        Question result = questionService.addQuestion(sampleQuestion);
        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());
        verify(questionDao, times(1)).save(sampleQuestion);
    }

    @Test
    void testEditQuestion() {
        sampleQuestion.setQuestion("Updated question?");
        when(questionDao.save(sampleQuestion)).thenReturn(sampleQuestion);

        Question result = questionService.editQuestion(sampleQuestion);

        assertEquals("Updated question?", result.getQuestion());
        verify(questionDao, times(1)).save(sampleQuestion);
    }

    @Test
    void testDeleteQuestion_Success() {
        Long questionId = 1L;
        when(questionDao.existsById(questionId)).thenReturn(true);
        doNothing().when(questionDao).deleteById(questionId);

        questionService.deleteQuestion(questionId);

        verify(questionDao).deleteById(questionId);
    }

    @Test
    void testDeleteQuestion_NotFound() {
        Long questionId = 99L;
        when(questionDao.existsById(questionId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> questionService.deleteQuestion(questionId)
        );

        assertEquals("Question not found with id: 99", exception.getMessage());
        verify(questionDao, never()).deleteById(anyLong());
    }

    @Test
    void testGetQuestionById_ReturnsWrapper() {
        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        QuestionWrapper result = questionService.getQuesitonById(1L);

        assertNotNull(result);
        assertEquals(sampleQuestion.getId(), result.getId());
        assertEquals(sampleQuestion.getQuestion(), result.getQuestion());
        assertEquals(sampleQuestion.getOption_a(), result.getOption_1());
        assertEquals(sampleQuestion.getCategoryName(), result.getCategory());

        verify(questionDao).findById(1L);
    }

    @Test
    void testGetQuestionById_NotFound() {
        when(questionDao.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuesitonById(99L));
        verify(questionDao).findById(99L);
    }

    @Test
    void testGetAllQuestions_ReturnsList() {
        List<Question> mockList = Arrays.asList(sampleQuestion);
        when(questionDao.findAll()).thenReturn(mockList);

        List<Question> result = questionService.getAllQuestions();

        assertEquals(1, result.size());
        assertEquals("What is Java?", result.get(0).getQuestion());
        verify(questionDao).findAll();
    }

    @Test
    void testGetQuestionsByCategory_Success()
    {
        Category category = sampleQuestion.getCategory();
        when(categoryDao.findById(101)).thenReturn(Optional.of(category));
        when(questionDao.findByCategory(category)).thenReturn(List.of(sampleQuestion));

        List<Question> result = questionService.getQuestionsByCategory(101);

        assertEquals(1,result.size());
        assertEquals("What is Java?", result.get(0).getQuestion());
        verify(categoryDao).findById(101);
        verify(questionDao).findByCategory(category);
    }

    @Test
    void testGetQuestionsByCategory_CategoryNotFound()
    {
        when(categoryDao.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,()->questionService.getQuestionsByCategory(999));
        verify(categoryDao).findById(999);
        verify(questionDao,never()).findByCategory(any());
    }

    @Test
    void testGetQuestionsForQuiz_ReturnsIds() {
        when(questionDao.findRandomQuestionsByCategory(101, 3)).thenReturn(List.of(1L, 2L, 3L));

        List<Long> ids = questionService.getQuestionsForQuiz(101, 3);

        assertEquals(3, ids.size());
        verify(questionDao).findRandomQuestionsByCategory(101, 3);
    }

    @Test
    void testGetQuestionsForManualQuiz_Success() {
        Category category = sampleQuestion.getCategory();
        when(categoryDao.findById(101)).thenReturn(Optional.of(category));
        when(questionDao.findRandomQuestionIds(category, "easy", 2)).thenReturn(List.of(10L, 11L));

        List<Long> ids = questionService.getQuestionsForManualQuiz(101, "easy", 2);

        assertEquals(2, ids.size());
        assertTrue(ids.contains(10L));
        verify(categoryDao).findById(101);
        verify(questionDao).findRandomQuestionIds(category, "easy", 2);
    }

    @Test
    void testGetQuestionsForManualQuiz_CategoryNotFound() {
        when(categoryDao.findById(404)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionsForManualQuiz(404, "medium", 5));
        verify(categoryDao).findById(404);
        verify(questionDao, never()).findRandomQuestionIds(any(), any(), anyInt());
    }

    @Test
    void testGetQuestionsFromId_success()
    {
        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        List<QuestionWrapper> result = questionService.getQuestionsFromId(List.of(1L));

        assertEquals(1, result.size());
        assertEquals(sampleQuestion.getQuestion(), result.get(0).getQuestion());
    }

    @Test
    void testGetScore_CorrectAnswer() {
        Response response = new Response();
        response.setQuiz_question_id(1L);
        response.setResponse("OOP language");
        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        int score = questionService.getScore(List.of(response));

        assertEquals(1, score);
    }

    @Test
    void testGetScore_WrongAnswer() {
        Response response = new Response();
        response.setQuiz_question_id(1L);
        response.setResponse("Database");

        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));

        int score = questionService.getScore(List.of(response));

        assertEquals(0, score);
    }

    @Test
    void testToggleQuestionStatus_Success() {
        sampleQuestion.setActive(false); // Allow toggle without external call

        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        when(questionDao.save(any())).thenReturn(sampleQuestion);

        Question result = questionService.toggleQuestionStatus(1L);

        assertTrue(result.isActive());
        verify(questionDao).save(sampleQuestion);
    }

    @Test
    void testToggleQuestionStatus_ActiveUsedInQuiz_ShouldThrow() {
        sampleQuestion.setActive(true);

        QuestionUsageResponse usageResponse = new QuestionUsageResponse();
        usageResponse.setUsed(true);
        usageResponse.setQuizTitle(List.of("Quiz A"));

        when(questionDao.findById(1L)).thenReturn(Optional.of(sampleQuestion));
        when(questionInterface.isQuestionUsedInActiveQuiz(1L))
                .thenReturn(new ResponseEntity<>(usageResponse, HttpStatus.OK));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> questionService.toggleQuestionStatus(1L));

        assertTrue(ex.getMessage().contains("Cannot deactivate question."));
    }

    @Test
    void testValidateQuestions_WithInactive() {
        sampleQuestion.setActive(false);
        when(questionDao.findAllById(List.of(1L))).thenReturn(List.of(sampleQuestion));

        Map<String, Object> result = questionService.validateQuestions(List.of(1L));

        assertEquals(1, result.get("totalCount"));
        assertEquals(0, result.get("activeCoubt"));
        assertEquals(1, result.get("inactiveCoubt"));
        assertEquals(false, result.get("allActive"));
    }

}
