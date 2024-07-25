package com.example.exams.Services;

import com.example.exams.Model.Data.db.Exam;
import com.example.exams.Repositories.Db.ExamRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ExamServiceTest {
    @Mock
    private ExamRepository examRepository;

    @Test
    public void testChangeExamVisibility() {
        MockitoAnnotations.openMocks(this);

        Exam exam1 = new Exam();
        exam1.setVisibility(false);
        when(examRepository.findById(1)).thenReturn(java.util.Optional.of(exam1));

        ExamService realExamService = new ExamService(examRepository, null, null, null, null, null, null);
        realExamService.changeExamVisibility(1);

        verify(examRepository, times(1)).findById(1);
        exam1 = realExamService.GetExam(1);
        assert(exam1.getVisibility());

        reset(examRepository);

        Exam exam2 = new Exam();
        exam2.setVisibility(true);
        when(examRepository.findById(2)).thenReturn(java.util.Optional.of(exam2));

        realExamService.changeExamVisibility(2);

        verify(examRepository, times(1)).findById(2);
        exam2 = realExamService.GetExam(2);
        assert(!exam2.getVisibility());
    }
}