package com.example.exams.Services;

import com.example.exams.Model.Data.db.Exam;
import com.example.exams.Model.Data.db.OpenQuestion;
import com.example.exams.Repositories.Db.OpenQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OpenQuestionServiceTest {

    @Mock
    private OpenQuestionRepository openQuestionRepository;

    @Mock
    private StudentOpenAnswerService studentOpenAnswerService;

    @InjectMocks
    private OpenQuestionService openQuestionService;

    private OpenQuestion openQuestion;
    private Exam exam;

    @BeforeEach
    void setUp() {
        openQuestion = new OpenQuestion();
        openQuestion.setOpenQuestionId(1);
        openQuestion.setContent("Sample content");
        openQuestion.setScore(5);

        exam = new Exam();
        exam.setQuestionPool(0);
        exam.setQuestionPoolStrategy(false);
        openQuestion.setExam(exam);

        when(openQuestionRepository.save(any(OpenQuestion.class))).thenReturn(openQuestion);
        when(openQuestionRepository.findById(anyInt())).thenReturn(Optional.of(openQuestion));
        when(openQuestionRepository.existsById(anyInt())).thenReturn(true);
    }

    @Test
    void testAddOpenQuestion() {
        OpenQuestion result = openQuestionService.AddOpenQuestion(openQuestion);
        assertNotNull(result);
        assertEquals(openQuestion.getContent(), result.getContent());
    }

    @Test
    void testUpdateOpenQuestion() {
        OpenQuestion updatedOpenQuestion = new OpenQuestion();
        updatedOpenQuestion.setOpenQuestionId(1);
        updatedOpenQuestion.setContent("Updated content");
        updatedOpenQuestion.setScore(10);

        OpenQuestion result = openQuestionService.UpdateOpenQuestion(updatedOpenQuestion);
        assertNotNull(result);
        assertEquals(updatedOpenQuestion.getContent(), result.getContent());
        assertEquals(updatedOpenQuestion.getScore(), result.getScore());
    }

    @Test
    void testGetOpenQuestion() {
        OpenQuestion result = openQuestionService.GetOpenQuestion(1);
        assertNotNull(result);
        assertEquals(openQuestion.getOpenQuestionId(), result.getOpenQuestionId());
    }

    @Test
    void testDeleteOpenQuestion() {
        boolean result = openQuestionService.deleteOpenQuestion(1);
        assertTrue(result);
        verify(openQuestionRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllOpenQuestionsByExamId() {
        List<OpenQuestion> questions = new ArrayList<>();
        questions.add(openQuestion);
        when(openQuestionRepository.findByExamId(anyInt())).thenReturn(questions);

        openQuestionService.deleteAllOpenQuestionsByExamId(1);

        verify(studentOpenAnswerService, times(1)).deleteAllAnswersByQuestionId(anyInt());
        verify(openQuestionRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testGetAll() {
        List<OpenQuestion> expectedList = new ArrayList<>();
        expectedList.add(openQuestion);
        when(openQuestionRepository.findAll()).thenReturn(expectedList);

        List<OpenQuestion> result = openQuestionService.getAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedList.size(), result.size());
    }

    @Test
    void testGetAllByExamId() {
        List<OpenQuestion> expectedList = new ArrayList<>();
        expectedList.add(openQuestion);
        when(openQuestionRepository.findByExamId(anyInt())).thenReturn(expectedList);

        List<OpenQuestion> result = openQuestionService.getAllByExamId(1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedList.size(), result.size());
    }

    @Test
    void testGetOpenQuestionById() {
        OpenQuestion result = openQuestionService.getOpenQuestionById("1");
        assertNotNull(result);
        assertEquals(openQuestion.getOpenQuestionId(), result.getOpenQuestionId());
    }
}
