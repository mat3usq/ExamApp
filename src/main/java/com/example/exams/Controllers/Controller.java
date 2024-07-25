package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.*;
import com.example.exams.Services.*;
import com.example.exams.SpringSecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import com.example.exams.Services.SubjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.exams.Model.Data.ProperDataModels.Login;
import com.example.exams.Model.Data.ProperDataModels.User;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    LoginsService loginsService;

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    UsersService usersService;
    @Autowired
    LogsService logsService;

    @Autowired
    StudentsService studentsService;
    @Autowired
    ExaminerService examinerService;
    @Autowired
    AdministartorService administartorService;


    public Controller(ExamService examService)
    {
        this.examService = examService;
    }

    @GetMapping("/")
    public ModelAndView StartPage(Model model) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session.getAttribute("visited") == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Servicestatistic servicestatistic = logsService.getServiceStatistic();
            int visitorscount = servicestatistic.getVisitorscount() + 1;
            servicestatistic.setVisitorscount(visitorscount);
            logsService.updateServicestatistic(servicestatistic);
            session.setAttribute("visited", true);
        }
        Servicestatistic servicestatistic = logsService.getServiceStatistic();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("servicestatistic", servicestatistic);
        return modelAndView;
    }

    @GetMapping("/editUser")
    public ModelAndView editUser(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editUser");
        return modelAndView;
    }

    @GetMapping("/aboutApp")
    public ModelAndView aboutApp(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("aboutApp");
        return modelAndView;
    }



    @GetMapping("/login")
    public ModelAndView login(Model model, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        model.addAttribute("login", new Login());
        return modelAndView;
    }
    @GetMapping("/logged")
    public ModelAndView logged(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        CustomUserDetails customUserDetails = (CustomUserDetails) session.getAttribute("UserDetails");
        if (session.getAttribute(customUserDetails.getUsername()+"_visited") == null) {
            Servicestatistic servicestatistic = logsService.getServiceStatistic();
            int visitorscount = servicestatistic.getVisitorscount() + 1;
            servicestatistic.setVisitorscount(visitorscount);
            logsService.updateServicestatistic(servicestatistic);
            session.setAttribute(customUserDetails.getUsername()+"_visited", true);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("logged");
        return modelAndView;
    }

    @GetMapping("/exams")
    public ModelAndView exams(
            @RequestParam(name = "searchQuery", required = false) String searchQuery,
            @RequestParam(name = "mindate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minDate,
            @RequestParam(name = "maxdate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxDate,
            @RequestParam(name = "sortType", required = false) String sortType)
    {
        ModelAndView modelAndView = new ModelAndView();


        List<Subject> subjects = subjectService.GetAll();
        modelAndView.addObject("subjects", subjects);
        List<Exam> exams = examService.getUserExams();

        if (searchQuery != null && !searchQuery.isEmpty()) {
            exams = examService.searchExams(searchQuery, exams);
        }
        if (minDate != null || maxDate != null) {
            exams = examService.getExamsDependsOnDates(minDate, maxDate,exams);
        } if(sortType != null){
            exams = examService.getSortedExams(sortType,exams);
        }
        modelAndView.addObject("exams", exams);
        modelAndView.setViewName("showExams");
        return modelAndView;
    }



    @GetMapping("/logs")
    public ModelAndView showLogs() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("logs", logsService.getLogs());
        modelAndView.setViewName("showLogs");
        return modelAndView;
    }

    @PostMapping("/results")
    public ModelAndView results() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("studentExamHistory");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView register(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        model.addAttribute("user", new User());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView  register(@ModelAttribute("user") User user) {
        String result = usersService.determinePermissions(user.getEmail());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);


        switch (result) {
            case "Student":
                if (session.getAttribute("studentregistration") == null) {
                    Servicestatistic servicestatistic = logsService.getServiceStatistic();
                    int studentCount = servicestatistic.getStudentscount()+1;
                    servicestatistic.setStudentscount(studentCount);
                    logsService.updateServicestatistic(servicestatistic);
                    session.setAttribute("studentregistration", true);
                }
                Student newStudent = usersService.mapToStudentEntity(user);
                Student foundStudentByLogin = studentsService.getStudentByLogin(newStudent.getLogin());
                Student foundStudentByEmail = studentsService.getStudentByEmail(newStudent.getEmail());
                if(foundStudentByLogin == null && foundStudentByEmail == null){
                    System.out.println("Student not found");
                    usersService.addAStudentToDB(newStudent);
                    return new ModelAndView("redirect:/login");
                }
                return new ModelAndView("register");
            case "Examiner":
                if (session.getAttribute("examinerregistration") == null) {
                    Servicestatistic servicestatistic = logsService.getServiceStatistic();
                    int examinerCount = servicestatistic.getExaminatorscount()+1;
                    servicestatistic.setExaminatorscount(examinerCount);
                    logsService.updateServicestatistic(servicestatistic);
                    session.setAttribute("examinerregistration", true);
                }
                Examiner newExaminer = usersService.mapToExaminerEntity(user);
                Examiner foundExaminerByLogin = examinerService.getExaminerByLogin(newExaminer.getLogin());
                Examiner foundExaminerByEmail = examinerService.getExaminerByEmail(newExaminer.getEmail());
                if(foundExaminerByLogin == null && foundExaminerByEmail == null){
                    System.out.println("Examiner not found");
                    usersService.addAExaminerToDB(newExaminer);
                    return new ModelAndView("redirect:/login");
                }
                return new ModelAndView("register");
            case "Administrator":
                Administrator newAdministrator = usersService.mapToAdministratorEntity(user);
                Administrator foundAdministratorByLogin = administartorService.getAdminByLogin(newAdministrator.getLogin());
                Administrator foundAdministratorByEmail = administartorService.getAdministratorByEmail(newAdministrator.getEmail());
                if(foundAdministratorByLogin == null && foundAdministratorByEmail == null){
                    System.out.println("Administrator not found");
                    usersService.addAdministratorToDB(newAdministrator);
                    return new ModelAndView("redirect:/login");
                }
                return new ModelAndView("register");
            default:
                return new ModelAndView("register");
        }
    }


}
