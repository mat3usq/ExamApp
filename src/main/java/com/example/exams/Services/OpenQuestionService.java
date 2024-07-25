package com.example.exams.Services;

import com.example.exams.Model.Data.db.Exam;
import com.example.exams.Model.Data.db.OpenQuestion;
import com.example.exams.Repositories.Db.OpenQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OpenQuestionService {
    private final OpenQuestionRepository openQuestionRepository;
    private final StudentOpenAnswerService studentOpenAnswerService;

    @Autowired
    public OpenQuestionService(OpenQuestionRepository openQuestionRepository, StudentOpenAnswerService studentOpenAnswerService){
        this.openQuestionRepository = openQuestionRepository;
        this.studentOpenAnswerService = studentOpenAnswerService;
    }

    public OpenQuestion UpdateOpenQuestion(OpenQuestion updatedOpenQuestion){
        OpenQuestion openQuestion = GetOpenQuestion(updatedOpenQuestion.getOpenQuestionId());
        if(openQuestion != null){
            openQuestion.setContent(updatedOpenQuestion.getContent());
            openQuestion.setScore(updatedOpenQuestion.getScore());
            //openQuestion.setQuestionSubject(updatedOpenQuestion.getQuestionSubject());
            return openQuestionRepository.save(openQuestion);
        }
        return null;
    }

    public OpenQuestion GetOpenQuestion(int questionID){
        Optional<OpenQuestion> existingOpenQuestionOptional = openQuestionRepository.findById(questionID);
        return existingOpenQuestionOptional.orElse(null);
    }

    public OpenQuestion AddOpenQuestion(OpenQuestion newOpenQuestion){
        Exam exam = newOpenQuestion.getExam();
        OpenQuestion openQuestion = openQuestionRepository.save(newOpenQuestion);
        if(!exam.getQuestionPoolStrategy()){
            exam.setQuestionPool(exam.getQuestionPool() + 1);
        }
        return openQuestion;
    }

    public List<OpenQuestion> getAll() {
        return openQuestionRepository.findAll();
    }

    public List<OpenQuestion> getAllByExamId(int examId) {
        return openQuestionRepository.findByExamId(examId);
    }

    public OpenQuestion getOpenQuestionById(String questionId) {
        int id = Integer.parseInt(questionId);
        return openQuestionRepository.findById(id).orElse(null);
    }


    @Transactional
    public boolean deleteOpenQuestion(Integer questionID) {
        if (openQuestionRepository.existsById(questionID)) {
            openQuestionRepository.deleteById(questionID);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteAllOpenQuestionsByExamId(int examId) {
        List<OpenQuestion> questions = openQuestionRepository.findByExamId(examId);
        for (OpenQuestion question : questions) {
            studentOpenAnswerService.deleteAllAnswersByQuestionId(question.getOpenQuestionId());
            openQuestionRepository.deleteById(question.getOpenQuestionId());
        }
    }
}