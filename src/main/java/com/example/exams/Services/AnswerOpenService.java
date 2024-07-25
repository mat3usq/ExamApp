package com.example.exams.Services;

import com.example.exams.Model.Data.db.Student;
import com.example.exams.Model.Data.db.Studentopenanswer;
import com.example.exams.Repositories.Db.LogstudentexamRepository;
import com.example.exams.Repositories.Db.StudentopenanswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerOpenService {

    private final StudentopenanswerRepository studentopenanswerRepository;

    private final LogstudentexamRepository logstudentexamRepository;
    @Autowired
    public AnswerOpenService(StudentopenanswerRepository studentopenanswerRepository, LogstudentexamRepository logstudentexamRepository){
        this.studentopenanswerRepository = studentopenanswerRepository;
        this.logstudentexamRepository = logstudentexamRepository;
    }
    public List<Student> getAllDistinctStudentsForOpenQuestions(int examId) {
        List<Studentopenanswer> studentopenanswers = studentopenanswerRepository.findAllByOpenquestionQuestionid_Exam_Id(examId);

        Set<Student> uniqueStudents = studentopenanswers.stream()
                .map(Studentopenanswer::getStudentStudent)
                .collect(Collectors.toSet());

        return sortByStudentId(List.copyOf(uniqueStudents));
    }
    public List<Student> sortByStudentId(List<Student> students) {
        List<Student> mutableList = new ArrayList<>(students);
        mutableList.sort(Comparator.comparing(Student::getStudentId));
        return mutableList;
    }

    public List<Studentopenanswer> getStudentOpenAnswerByStudent(Student student){
        return studentopenanswerRepository.findAllByStudentStudent(student);
    }

    public int updateScores(Student student, List<Integer> scores) {
        List<Studentopenanswer> studentOpenAnswers = getStudentOpenAnswerByStudent(student);
        int points = 0;
        if (studentOpenAnswers.size() != scores.size()) {
            throw new IllegalArgumentException("List sizes do not match");
        }

        for (int i = 0; i < studentOpenAnswers.size(); i++) {
            Studentopenanswer answer = studentOpenAnswers.get(i);
            Integer score = scores.get(i);
            if (answer.getScore() == null){
                answer.setScore(score);
                points += score;
            }else{
                int a = answer.getScore();
                answer.setScore(score);
                points += score - a;
            }
            studentopenanswerRepository.save(answer);
        }

        return points;

    }
}
