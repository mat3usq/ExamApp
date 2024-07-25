package com.example.exams.Services;

import com.example.exams.Model.Data.db.*;
import com.example.exams.Repositories.Db.LogstudentexamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class LogstudentexamService {
    private final LogstudentexamRepository logstudentexamRepository;
    private final ClosedQuestionService closedQuestionService;
    private final OpenQuestionService openQuestionService;
    private final ExamService examService;

    public LogstudentexamService(LogstudentexamRepository logstudentexamRepository, ClosedQuestionService closedQuestionService, OpenQuestionService openQuestionService, ExamService examService) {
        this.logstudentexamRepository = logstudentexamRepository;
        this.closedQuestionService = closedQuestionService;
        this.openQuestionService = openQuestionService;
        this.examService = examService;
    }

    public List<Logstudentexam> getStudentExamHistory(Integer id) {
        return logstudentexamRepository.findByStudentStudentId(id);
    }

    public List<Logstudentexam> getStudentsLogstudentExamById(Exam exam) {
        return logstudentexamRepository.findLogstudentexamsByExamExamid(exam);
    }

    public void setDateTimeExaminerComment(Student student, Exam exam, String examinerComment){
        Logstudentexam logstudentexam = logstudentexamRepository.findLogstudentexamByStudentStudentAndExamExamid(student, exam);
        logstudentexam.setDate(LocalDate.now());
        logstudentexam.setTime(LocalTime.now());
        logstudentexam.setDescription(examinerComment);
        logstudentexamRepository.save(logstudentexam);
    }


    public void addOpenPoints(Student student, Exam exam, Integer openPoints){
        Logstudentexam logstudentexam = logstudentexamRepository.findLogstudentexamByStudentStudentAndExamExamid(student, exam);
        int score;

        if (logstudentexam.getScoreresult() == null){
            score = 0;
        }else{
            score = logstudentexam.getScoreresult();
        }
        int finalResult = score + openPoints;
        logstudentexam.setScoreresult(finalResult);
        logstudentexamRepository.save(logstudentexam);
    }


    public void addPointsToLogstudentexam(int logstudentexamId, int points) {
        Logstudentexam logstudentexam = logstudentexamRepository.findById(logstudentexamId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono Logstudentexam o ID: " + logstudentexamId));

        logstudentexam.addPoints(points);
        logstudentexamRepository.save(logstudentexam);
    }

    public Integer createAndSaveLogstudentexam(Exam examExamid, Student studentStudent) {
        Logstudentexam newLogstudentexam = new Logstudentexam();
        newLogstudentexam.setDate(LocalDate.now());
        newLogstudentexam.setTime(LocalTime.now());
        newLogstudentexam.setExamExamid(examExamid);
        newLogstudentexam.setStudentStudent(studentStudent);
        logstudentexamRepository.save(newLogstudentexam);
        return newLogstudentexam.getId();
    }
    @Transactional
    public void deleteAllLogsForExam(Exam exam) {
        logstudentexamRepository.deleteByExamExamid(exam);
    }

    public boolean existsByExamIdAndStudentId(Integer examId, Integer studentId) {
        return logstudentexamRepository.existsByExamExamid_IdAndStudentStudent_Id(examId, studentId);
    }

    public int getExamMaximumScore(int exam){
        List<Closedquestion> closedQuestions = closedQuestionService.getAllByExamId(exam);
        List<OpenQuestion> openQuestions = openQuestionService.getAllByExamId(exam);
        int maximumScore = 0;
        for(int i = 0; i < closedQuestions.size(); ++i){
            maximumScore += closedQuestions.get(i).getScore();
        }
        for(int i = 0; i < openQuestions.size(); ++i){
            maximumScore += openQuestions.get(i).getScore();
        }
        return maximumScore;
    }

    private int getXGrades(int examId, float multiplier){
        int maximumScore = getExamMaximumScore(examId);
        Exam exam = examService.GetExam(examId);
        List<Logstudentexam> logstudentexams = logstudentexamRepository.findLogstudentexamsByExamExamid(exam);
        int xGrades = 0;
        float lowerLimit = multiplier;
        float upperLimit = Math.round((multiplier + 0.1f) * 10) / 10.0f;
        System.out.println("-------------------");
        System.out.println("Maximum score: " + maximumScore);
        System.out.println("Lower limit: " + lowerLimit + " Upper limit: " + upperLimit);
        float receivedScore;
        for (Logstudentexam logstudentexam : logstudentexams) {
            receivedScore = (float) logstudentexam.getScoreresult() / maximumScore;
            System.out.println("Received score: " + receivedScore);
            System.out.println("-------------------");
            if (receivedScore >= lowerLimit && receivedScore < upperLimit) {
                xGrades++;
            }
        }
        return xGrades;
    }

    public int getAGrades(int examId){
        return getXGrades(examId, 0.9f);
    }
    public int getBPlusGrades(int examId){
        return getXGrades(examId, 0.8f);
    }
    public int getBGrades(int examId){
        return getXGrades(examId, 0.7f);
    }
    public int getCPlusGrades(int examId){
        return getXGrades(examId, 0.6f);
    }
    public int getCGrades(int examId){
        return getXGrades(examId, 0.5f);
    }
    public int getDGrades(int examId){
        int maximumScore = getExamMaximumScore(examId);
        Exam exam = examService.GetExam(examId);
        List<Logstudentexam> logstudentexams = logstudentexamRepository.findLogstudentexamsByExamExamid(exam);
        int DGrades = 0;
        float upperLimit = 0.5f;
        float receivedScore;
        for (Logstudentexam logstudentexam : logstudentexams) {
            receivedScore = (float) logstudentexam.getScoreresult() / maximumScore;
            if (receivedScore < upperLimit) {
                DGrades++;
            }
        }
        return DGrades;
    }
}


