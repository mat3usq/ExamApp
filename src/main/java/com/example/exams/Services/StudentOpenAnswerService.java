package com.example.exams.Services;

import com.example.exams.Model.Data.db.Studentopenanswer;
import com.example.exams.Repositories.Db.StudentopenanswerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.exams.Model.Data.db.OpenQuestion;
import com.example.exams.Repositories.Db.OpenQuestionRepository;


import java.util.List;

@Service
public class StudentOpenAnswerService {

    private final StudentopenanswerRepository studentopenanswerRepository;
    private final OpenQuestionRepository openQuestionRepository;

    @Autowired
    public StudentOpenAnswerService(StudentopenanswerRepository studentopenanswerRepository, OpenQuestionRepository openQuestionRepository) {
        this.studentopenanswerRepository = studentopenanswerRepository;
        this.openQuestionRepository = openQuestionRepository;
    }

    @Transactional
    public void deleteAllAnswersByQuestionId(Integer openQuestionId) {
        OpenQuestion openQuestion = openQuestionRepository.findById(openQuestionId)
                .orElseThrow(() -> new EntityNotFoundException("OpenQuestion not found with id: " + openQuestionId));
        List<Studentopenanswer> answers = studentopenanswerRepository.findByOpenquestionQuestionid(openQuestion);
        studentopenanswerRepository.deleteAll(answers);
    }


    @Transactional
    public Studentopenanswer saveStudentOpenAnswer(Studentopenanswer studentOpenAnswer) {
        return studentopenanswerRepository.save(studentOpenAnswer);
    }




}
