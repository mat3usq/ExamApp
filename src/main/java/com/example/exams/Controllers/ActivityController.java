package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Group;
import com.example.exams.Services.AdministartorService;
import com.example.exams.Services.ExaminerService;
import com.example.exams.Services.GroupsService;
import com.example.exams.Services.StudentsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ActivityController {
    final ExaminerService examinerService;
    final AdministartorService administartorService;

    public ActivityController(ExaminerService examinerService, AdministartorService administartorService) {
        this.examinerService = examinerService;
        this.administartorService = administartorService;
    }

    @GetMapping("/activity")
    public ModelAndView activity() {
        List<Examiner> examiners = examinerService.getAllExaminers();
        List<Administrator> administrators = administartorService.getAllAdministrators();
        System.out.println("Examiners: " + examiners);
        System.out.println("Administrators: " + administrators);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("activity");
        modelAndView.addObject("examiners", examiners);
        modelAndView.addObject("administrators", administrators);
        return modelAndView;
    }

    @PostMapping("/examiner/activate/{id}")
    public String activateExaminer(@PathVariable int id) {
        examinerService.activate(id);
        return "redirect:/activity";
    }

    @PostMapping("/examiner/deactivate/{id}")
    public String deactivateExaminer(@PathVariable int id) {
        examinerService.deactivate(id);
        return "redirect:/activity";
    }

    @PostMapping("/administrator/activate/{id}")
    public String activateAdministrator(@PathVariable int id) {
        administartorService.activate(id);
        return "redirect:/activity";
    }

    @PostMapping("/administrator/deactivate/{id}")
    public String deactivateAdministrator(@PathVariable int id) {
        administartorService.deactivate(id);
        return "redirect:/activity";
    }
}
