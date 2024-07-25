package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Closedquestion;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Model.Data.db.Studentclosedanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentclosedanswerRepository extends JpaRepository<Studentclosedanswer, Integer> {
    List<Studentclosedanswer> findAllByAnswerclosedAnswerid_ClosedquestionQuestionid_Exam_Id(int examId);

    List<Studentclosedanswer> findAllByAnswerclosedAnswerid_ClosedquestionQuestionid_Exam_Id_AndStudentStudent_StudentId(int examId, int studentId);



    @Modifying
    @Transactional
    @Query("delete from Studentclosedanswer s where s.answerclosedAnswerid.closedquestionQuestionid.id = :questionId")
    void deleteByQuestionId(@Param("questionId") int questionId);

    @Modifying
    @Transactional
    @Query("delete from Studentclosedanswer s where s.id = :answerId")
    void deleteByAnswerId(@Param("answerId") int answerId);

}