package com.example.exams.Controllers;


import com.example.exams.Model.Data.ProperDataModels.User;
import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Model.Data.db.Subject;
import com.example.exams.Services.AdministartorService;
import com.example.exams.Services.ExaminerService;
import com.example.exams.Services.StudentsService;
import com.example.exams.Services.UsersService;
import com.example.exams.SpringSecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class MyAccountController {

    @Autowired
    StudentsService studentsService;
    @Autowired
    ExaminerService examinerService;
    @Autowired
    AdministartorService administartorService;
    @Autowired
    UsersService usersService;

    public MyAccountController(StudentsService studentsService, ExaminerService examinerService, AdministartorService administartorService, UsersService usersService) {
        this.studentsService = studentsService;
        this.examinerService = examinerService;
        this.administartorService = administartorService;
        this.usersService = usersService;
    }

        @GetMapping("/tipOfTheDay")
        public String tipOfTheDay(Model model) {
            // Dodaj logikę do pobierania wskazówki dnia i dodaj ją do modelu
            return "tipOfTheDay"; // Nazwa widoku, który ma być wyrenderowany
        }

    @GetMapping("/myAccount")
    public ModelAndView myAccount(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("myAccount");

        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            setUserModel(model, request);
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }
        assert user != null;
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                model.addAttribute("permission", authority.getAuthority().substring(5));
                model.addAttribute("user", administartorService.getAdminById(user.getUserId()));
                break;
            }
            if ("ROLE_EXAMINER".equals(authority.getAuthority())) {
                model.addAttribute("permission", authority.getAuthority().substring(5));
                model.addAttribute("user", examinerService.Get(user.getUserId()));
                break;
            }
            if ("ROLE_STUDENT".equals(authority.getAuthority())) {
                model.addAttribute("permission", authority.getAuthority().substring(5));
                model.addAttribute("user", studentsService.getStudentById(user.getUserId()));
                break;
            }
        }
        return modelAndView;
    }


    @PostMapping("/myAccount")
    public String updateUser(@Valid @ModelAttribute("user") User usersave, BindingResult result, Model model) {

        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }
        if (result.hasErrors()) {
            setUserModel(model,request);
            return "myAccount";
        }

        assert user != null;
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                Administrator administrator = administartorService.getAdminById(user.getUserId());
                administartorService.editAdministator(administrator.getAdministrator_id(),usersave.getFirstname()
                        ,usersave.getLastname(),usersave.getLogin()
                        ,usersave.getPassword(),usersave.getEmail());
                break;
            }
            if ("ROLE_EXAMINER".equals(authority.getAuthority())) {

                Examiner examiner = examinerService.findByLogin(user.getUsername());
                examinerService.editExaminer(examiner.getExaminer_id(),usersave.getFirstname()
                        ,usersave.getLastname(),usersave.getLogin()
                        ,usersave.getPassword(),usersave.getEmail());
                break;
            }
            if ("ROLE_STUDENT".equals(authority.getAuthority())) {

                Student student = studentsService.getStudentById(user.getUserId());
                studentsService.editStudent(student.getStudentId(),usersave.getFirstname()
                        ,usersave.getLastname(),usersave.getLogin()
                        ,usersave.getPassword(),usersave.getEmail());
                break;
            }
        }

        return "redirect:/myAccount"; // redirect to a confirmation page or the updated user profile
    }

    private void setUserModel(Model model, HttpServletRequest request) {
        CustomUserDetails user = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }
        if (user != null) {
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    model.addAttribute("permission", authority.getAuthority().substring(5));
                   // model.addAttribute("user", administartorService.getAdminById(user.getUserId()));
                    break;
                }
                if ("ROLE_EXAMINER".equals(authority.getAuthority())) {
                    model.addAttribute("permission", authority.getAuthority().substring(5));
                    //model.addAttribute("user", examinerService.Get(user.getUserId()));
                    break;
                }
                if ("ROLE_STUDENT".equals(authority.getAuthority())) {
                    model.addAttribute("permission", authority.getAuthority().substring(5));
                   // model.addAttribute("user", studentsService.getStudentById(user.getUserId()));
                    break;
                }
            }
        }
        }

    }
