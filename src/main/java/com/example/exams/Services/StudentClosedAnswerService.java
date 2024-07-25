package com.example.exams.Services;

import com.example.exams.Model.Data.db.Studentclosedanswer;
import com.example.exams.Repositories.Db.StudentclosedanswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentClosedAnswerService {

    @Autowired
    private StudentclosedanswerRepository studentClosedAnswerRepository;

    public void saveStudentClosedAnswer(Studentclosedanswer closedAnswer) {
        studentClosedAnswerRepository.save(closedAnswer);
    }

    public void deleteStudentClosedAnswersByQuestionId(int questionId) {
        studentClosedAnswerRepository.deleteByQuestionId(questionId);
    }

    public void deleteStudentClosedAnswerByAnswerId(int answerId) {
        studentClosedAnswerRepository.deleteByAnswerId(answerId);
    }


}
