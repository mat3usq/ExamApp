package com.example.exams.Services;

import com.example.exams.Model.Data.db.Student;
import com.example.exams.Model.Data.db.Studentopenanswer;
import com.example.exams.Repositories.Db.LogstudentexamRepository;
import com.example.exams.Repositories.Db.StudentopenanswerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AnswerOpenServiceTest {

    @Mock
    private StudentopenanswerRepository studentopenanswerRepository;

    @Mock
    private LogstudentexamRepository logstudentexamRepository;

    @InjectMocks
    private AnswerOpenService answerOpenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDistinctStudentsForOpenQuestions() {
        int examId = 1;
        Student student1 = new Student();
        student1.setStudentId(1);
        Student student2 = new Student();
        student2.setStudentId(2);

        Studentopenanswer studentOpenAnswer1 = new Studentopenanswer();
        studentOpenAnswer1.setStudentStudent(student1);
        Studentopenanswer studentOpenAnswer2 = new Studentopenanswer();
        studentOpenAnswer2.setStudentStudent(student2);
        Studentopenanswer studentOpenAnswer3 = new Studentopenanswer();
        studentOpenAnswer3.setStudentStudent(student1);

        List<Studentopenanswer> studentOpenAnswers = Arrays.asList(studentOpenAnswer1, studentOpenAnswer2, studentOpenAnswer3);
        when(studentopenanswerRepository.findAllByOpenquestionQuestionid_Exam_Id(examId)).thenReturn(studentOpenAnswers);

        List<Student> result = answerOpenService.getAllDistinctStudentsForOpenQuestions(examId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student2));
        verify(studentopenanswerRepository).findAllByOpenquestionQuestionid_Exam_Id(examId);
    }

    @Test
    void testSortByStudentId() {
        List<Student> students = new ArrayList<>();
        Student student1 = new Student();
        student1.setStudentId(2);
        Student student2 = new Student();
        student2.setStudentId(1);
        students.add(student1);
        students.add(student2);

        List<Student> sortedStudents = answerOpenService.sortByStudentId(students);

        assertEquals(2, sortedStudents.size());
        assertEquals(1, sortedStudents.get(0).getStudentId());
        assertEquals(2, sortedStudents.get(1).getStudentId());
    }

    @Test
    void testGetStudentOpenAnswerByStudent() {
        Student student = new Student();
        List<Studentopenanswer> studentOpenAnswers = Arrays.asList(new Studentopenanswer(), new Studentopenanswer());
        when(studentopenanswerRepository.findAllByStudentStudent(student)).thenReturn(studentOpenAnswers);

        List<Studentopenanswer> result = answerOpenService.getStudentOpenAnswerByStudent(student);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentopenanswerRepository).findAllByStudentStudent(student);
    }

    @Test
    void testUpdateScores() {
        Student student = new Student();
        List<Integer> scores = Arrays.asList(5, 10);
        List<Studentopenanswer> studentOpenAnswers = new ArrayList<>();
        Studentopenanswer answer1 = new Studentopenanswer();
        answer1.setScore(null);
        Studentopenanswer answer2 = new Studentopenanswer();
        answer2.setScore(null);
        studentOpenAnswers.add(answer1);
        studentOpenAnswers.add(answer2);
        when(studentopenanswerRepository.findAllByStudentStudent(any(Student.class))).thenReturn(studentOpenAnswers);

        int result = answerOpenService.updateScores(student, scores);

        assertEquals(15, result);
        verify(studentopenanswerRepository, times(scores.size())).save(any(Studentopenanswer.class));
    }

    @Test
    void testUpdateScoresWithMismatchedListSizes() {
        Student student = new Student();
        List<Integer> scores = Collections.singletonList(5);
        List<Studentopenanswer> studentOpenAnswers = Arrays.asList(new Studentopenanswer(), new Studentopenanswer());
        when(studentopenanswerRepository.findAllByStudentStudent(student)).thenReturn(studentOpenAnswers);

        assertThrows(IllegalArgumentException.class, () -> answerOpenService.updateScores(student, scores));
    }
}
