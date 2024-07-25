package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentsEntityRepository extends JpaRepository<Student, Integer> {
    Student findStudentByLogin(String login);
    Student findStudentByEmail(String email);
    List<Student> findAll();
    Student findStudentByStudentId(Integer studentId);

}
