package com.example.exams.Services;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Repositories.Db.AdministratorsEntityRepository;
import com.example.exams.Repositories.Db.ExaminerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doAnswer;

public class AdministratorServiceTest {
    private final AdministratorsEntityRepository administratorsRepository = Mockito.mock(AdministratorsEntityRepository.class);
    private final AdministartorService administratorService = new AdministartorService(administratorsRepository);

    @Test
    public void testActivate() {
        Administrator administrator = new Administrator();
        administrator.setVerificationStatus(false);

        Mockito.when(administratorsRepository.findById(any(Integer.class))).thenReturn(Optional.of(administrator));

        administratorService.activate(1);

        verify(administratorsRepository, times(1)).save(administrator);
        assert(administrator.isVerificationStatus());
    }

    @Test
    public void testDeactivate() {
        Administrator administrator = new Administrator();
        administrator.setVerificationStatus(true);

        Mockito.when(administratorsRepository.findById(any(Integer.class))).thenReturn(Optional.of(administrator));

        administratorService.deactivate(1);

        verify(administratorsRepository, times(1)).save(administrator);
        assert(!administrator.isVerificationStatus());
    }

    @Test
    public void testDelete() {
        List<Administrator> administrators = new ArrayList<>();
        Administrator administrator1 = new Administrator(1, "John", "Doe", "johndoe", "password", "johndoe@example.com", true);
        Administrator administrator2 = new Administrator(2, "Jane", "Doe", "janedoe", "password", "janedoe@example.com", true);
        administrators.add(administrator1);
        administrators.add(administrator2);

        when(administratorsRepository.findById(1)).thenReturn(Optional.of(administrator1));
        doAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            administrators.removeIf(administrator -> administrator.getAdministrator_id().equals(id));
            return null;
        }).when(administratorsRepository).deleteById(any(Integer.class));

        administratorService.deleteAdministrator(1);

        Optional<Administrator> deletedExaminer = administrators.stream().filter(examiner -> examiner.getAdministrator_id().equals(1)).findFirst();
        assertFalse(deletedExaminer.isPresent());

        verify(administratorsRepository, times(1)).deleteById(1);
    }
}
