package com.example.exams.Services;

import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Repositories.Db.ExaminerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExaminerServiceTest {

    private final ExaminerRepository examinerRepository = Mockito.mock(ExaminerRepository.class);
    private final ExaminerService examinerService = new ExaminerService(examinerRepository);

    @Test
    public void testActivate() {
        Examiner examiner = new Examiner();
        examiner.setVerificationStatus(false);

        when(examinerRepository.findById(any(Integer.class))).thenReturn(Optional.of(examiner));

        examinerService.activate(1);

        verify(examinerRepository, times(1)).save(examiner);
        assert(examiner.isVerificationStatus());
    }

    @Test
    public void testDeactivate() {
        Examiner examiner = new Examiner();
        examiner.setVerificationStatus(true);

        when(examinerRepository.findById(any(Integer.class))).thenReturn(Optional.of(examiner));

        examinerService.deactivate(1);

        verify(examinerRepository, times(1)).save(examiner);
        assert(!examiner.isVerificationStatus());
    }

    @Test
    public void testDelete() {
        List<Examiner> examiners = new ArrayList<>();
        Examiner examiner1 = new Examiner(1, "John", "Doe", "johndoe", "password", "johndoe@example.com", true);
        Examiner examiner2 = new Examiner(2, "Jane", "Doe", "janedoe", "password", "janedoe@example.com", true);
        examiners.add(examiner1);
        examiners.add(examiner2);

        when(examinerRepository.findById(1)).thenReturn(Optional.of(examiner1));
        doAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            examiners.removeIf(examiner -> examiner.getExaminer_id().equals(id));
            return null;
        }).when(examinerRepository).deleteById(any(Integer.class));

        examinerService.deleteExaminer(1);

        Optional<Examiner> deletedExaminer = examiners.stream().filter(examiner -> examiner.getExaminer_id().equals(1)).findFirst();
        assertFalse(deletedExaminer.isPresent());

        verify(examinerRepository, times(1)).deleteById(1);
    }
}