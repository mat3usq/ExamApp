package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExaminerRepository extends JpaRepository<Examiner, Integer> {
    Examiner findExaminerByLogin(String login);
    Examiner findByLogin(String login);
    Examiner findExaminerByEmail(String email);
    @Query("SELECT e FROM Examiner e WHERE e.examiner_id = :id")
    Examiner findExaminerById(@Param("id") Integer id);

    List<Examiner> findAll();
}
