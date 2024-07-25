package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Closedquestion;
import com.example.exams.Model.Data.db.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClosedQuestionRepository extends JpaRepository<Closedquestion, Integer> {
    List<Closedquestion> findByExamId(Integer examId);
}