package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.Logstudentexam;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Repositories.Db.StudentsEntityRepository;
import com.example.exams.SpringSecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.exams.Services.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ExamResultsController {

    @Autowired
    private LogstudentexamService logstudentexamService;
    @Autowired
    private StudentsEntityRepository studentsEntityRepository;

    @GetMapping("/results")
    public String studentExamHistory(Model model, HttpServletRequest request) {
        //System.out.println("odpalił się kontroler");
        CustomUserDetails user = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }

        if (user == null) {
            // Handle case where user is not found or not logged in
            return "errorPage"; // Redirect to an error page or login page
        }

        Student student = studentsEntityRepository.findStudentByLogin(user.getUsername());
        if (student == null) {
            // Handle case where student is not found
            return "errorPage"; // Redirect to an error page or login page
        }

        int studentId = student.getStudentId();
        List<Logstudentexam> examHistory = logstudentexamService.getStudentExamHistory(studentId);
        model.addAttribute("examHistory", examHistory);
        return "studentExamHistory"; // Make sure this matches the name of your Thymeleaf template
    }
}
