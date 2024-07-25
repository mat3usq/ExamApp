package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Student;
import com.example.exams.Model.Data.db.OpenQuestion;
import com.example.exams.Model.Data.db.Studentclosedanswer;
import com.example.exams.Model.Data.db.Studentopenanswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentopenanswerRepository extends JpaRepository<Studentopenanswer, Integer> {
    List<Studentopenanswer> findAllByOpenquestionQuestionid_Exam_Id(int examId);
    List<Studentopenanswer> findAllByStudentStudent(Student student);
    List<Studentopenanswer> findByOpenquestionQuestionid(OpenQuestion openQuestion);

}