package com.example.exams.Services;

import com.example.exams.Model.Data.db.Closedquestion;
import com.example.exams.Model.Data.db.Exam;
import com.example.exams.Repositories.Db.ClosedQuestionRepository;
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
public class ClosedQuestionServiceTest {

    @Mock
    private ClosedQuestionRepository closedQuestionRepository;

    @Mock
    private StudentClosedAnswerService studentClosedAnswerService;

    @Mock
    private AnswerClosedService answerClosedService;

    @InjectMocks
    private ClosedQuestionService closedQuestionService;

    private Closedquestion closedQuestion;
    private Exam exam;

    @BeforeEach
    void setUp() {
        closedQuestion = new Closedquestion();
        closedQuestion.setId(1);
        closedQuestion.setContent("Sample content");
        closedQuestion.setScore(5);

        exam = new Exam();
        exam.setQuestionPool(0);
        exam.setQuestionPoolStrategy(false);
        closedQuestion.setExam(exam);

        when(closedQuestionRepository.save(any(Closedquestion.class))).thenReturn(closedQuestion);
        when(closedQuestionRepository.findById(anyInt())).thenReturn(Optional.of(closedQuestion));
        when(closedQuestionRepository.existsById(anyInt())).thenReturn(true);
    }

    @Test
    void testAddClosedQuestion() {
        Closedquestion result = closedQuestionService.addClosedQuestion(closedQuestion);
        assertNotNull(result);
        assertEquals(closedQuestion.getContent(), result.getContent());
    }

    @Test
    void testSaveClosedQuestion() {
        Closedquestion result = closedQuestionService.save(closedQuestion);
        assertNotNull(result);
        assertEquals(closedQuestion.getContent(), result.getContent());
    }

    @Test
    void testGetClosedQuestionById() {
        Closedquestion result = closedQuestionService.getClosedQuestionById(1);
        assertNotNull(result);
        assertEquals(closedQuestion.getId(), result.getId());
    }

    @Test
    void testDeleteClosedQuestion() {
        closedQuestionService.deleteClosedQuestion(1);
        verify(closedQuestionRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllClosedQuestionsByExamId() {
        List<Closedquestion> questions = new ArrayList<>();
        questions.add(closedQuestion);
        when(closedQuestionRepository.findByExamId(anyInt())).thenReturn(questions);

        closedQuestionService.deleteAllClosedQuestionsByExamId(1);

        verify(studentClosedAnswerService, times(1)).deleteStudentClosedAnswersByQuestionId(anyInt());
        verify(answerClosedService, times(1)).deleteAnswersByQuestionId(anyInt());
        verify(closedQuestionRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void testGetAllByExamId() {
        List<Closedquestion> expectedList = new ArrayList<>();
        expectedList.add(closedQuestion);
        when(closedQuestionRepository.findByExamId(anyInt())).thenReturn(expectedList);

        List<Closedquestion> result = closedQuestionService.getAllByExamId(1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(expectedList.size(), result.size());
    }
}
