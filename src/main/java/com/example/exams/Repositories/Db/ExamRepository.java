package com.example.exams.Repositories.Db;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.exams.Model.Data.db.Exam;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findByEndDateAfter(LocalDate date);
}