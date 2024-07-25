package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Services.AdministartorService;
import com.example.exams.Services.ExaminerService;
import com.example.exams.Services.StudentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AdministrationController {
    final AdministartorService administratorService;
    final ExaminerService examinerService;
    final StudentsService studentsService;

    public AdministrationController(AdministartorService administratorService, ExaminerService examinerService, StudentsService studentsService) {
        this.administratorService = administratorService;
        this.examinerService = examinerService;
        this.studentsService = studentsService;
    }

    @GetMapping("/delete")
    public ModelAndView getAllAccounts() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deleteAccount");
        List<Administrator> administrators = administratorService.getAllAdministrators();
        List<Examiner> examiners = examinerService.getAllExaminers();
        List<Student> students = studentsService.getAllStudents();
        modelAndView.addObject("administrators", administratorService.getAllAdministrators());
        modelAndView.addObject("examiners", examinerService.getAllExaminers());
        modelAndView.addObject("students", studentsService.getAllStudents());
        return modelAndView;
    }

    @PostMapping("/delete/administrator/{id}")
    public String deleteAdministrator(@PathVariable int id) {
        administratorService.deleteAdministrator(id);
        return "redirect:/delete";
    }

    @PostMapping("/delete/examiner/{id}")
    public String deleteExaminer(@PathVariable int id) {
        examinerService.deleteExaminer(id);
        return "redirect:/delete";
    }

    @PostMapping("/delete/student/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentsService.deleteStudent(id);
        return "redirect:/delete";
    }
}
