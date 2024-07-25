package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.OpenQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpenQuestionRepository extends JpaRepository<OpenQuestion, Integer> {
    List<OpenQuestion> findByExamId(Integer examId);
}