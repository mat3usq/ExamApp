package com.example.exams.Controllers;

import com.example.exams.Services.LogstudentexamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ExamResultController {
    private final LogstudentexamService logstudentexamService;

    public ExamResultController(LogstudentexamService logstudentexamService){
        this.logstudentexamService = logstudentexamService;
    }

    @GetMapping("/exam/{examId}/grades")
    public ModelAndView getExamGrades(@PathVariable int examId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("examResult");
        return modelAndView;
    }

    @GetMapping("/api/exam/{examId}/grades")
    public Map<String, Integer> getExamGradesData(@PathVariable int examId) {
        Map<String, Integer> grades = new HashMap<>();
        grades.put("A", logstudentexamService.getAGrades(examId));
        grades.put("B+", logstudentexamService.getBPlusGrades(examId));
        grades.put("B", logstudentexamService.getBGrades(examId));
        grades.put("C+", logstudentexamService.getCPlusGrades(examId));
        grades.put("C", logstudentexamService.getCGrades(examId));
        grades.put("D", logstudentexamService.getDGrades(examId));
        return grades;
    }
}