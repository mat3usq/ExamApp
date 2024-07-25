package com.example.exams.Services;

import com.example.exams.Model.Data.db.*;
import com.example.exams.Repositories.Db.ExamRepository;
import com.example.exams.Repositories.Db.LogstudentexamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class LogStudentExamServiceTest {
    @InjectMocks
    private LogstudentexamService logstudentexamService;

    @Mock
    private LogstudentexamRepository logstudentexamRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ClosedQuestionService closedQuestionService;

    @Mock
    private OpenQuestionService openQuestionService;

    @Mock
    private ExamService examService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getExamMaximumScore() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        when(examRepository.getReferenceById(1)).thenReturn(exam1);

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 10, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 1, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        int result = logstudentexamService.getExamMaximumScore(1);

        assertEquals(11, result);
    }

    @Test
    public void getAGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 90, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 95, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 55, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 60, exam1,  new Student())
        ));
        int result = logstudentexamService.getAGrades(1);
        assertEquals(2, result);
    }

    @Test
    public void getBPlusGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 80, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 85, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 55, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 90, exam1,  new Student())
        ));
        int result = logstudentexamService.getBPlusGrades(1);
        assertEquals(2, result);
    }

    @Test
    public void getBGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 70, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 75, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 55, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 80, exam1,  new Student())
        ));
        int result = logstudentexamService.getBGrades(1);
        assertEquals(2, result);
    }

    @Test
    public void getCPlusGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 60, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 65, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 55, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 70, exam1,  new Student())
        ));
        int result = logstudentexamService.getCPlusGrades(1);
        assertEquals(2, result);
    }

    @Test
    public void getCGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 50, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 55, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 75, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 60, exam1,  new Student())
        ));
        int result = logstudentexamService.getCGrades(1);
        assertEquals(2, result);
    }

    @Test
    public void getDGrades() {
        Exam exam1 = new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), new Subject(1, "IT"), new ArrayList<>());

        OpenQuestion openQuestion1 = new OpenQuestion(1, "Ile to 5+5?", 50, examRepository.getReferenceById(1));
        Closedquestion closedquestion1 = new Closedquestion(1, "ile to jest 10*10", 50, examRepository.getReferenceById(1));

        when(closedQuestionService.getAllByExamId(1)).thenReturn(List.of(closedquestion1));
        when(openQuestionService.getAllByExamId(1)).thenReturn(List.of(openQuestion1));

        when(examService.GetExam(1)).thenReturn(exam1);
        when(logstudentexamRepository.findLogstudentexamsByExamExamid(exam1)).thenReturn(Arrays.asList(
                new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), 45, exam1,  new Student()),
                new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), 1, exam1,  new Student()),
                new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), 50, exam1,  new Student()),
                new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), 60, exam1,  new Student())
        ));
        int result = logstudentexamService.getDGrades(1);
        assertEquals(2, result);
    }
}
