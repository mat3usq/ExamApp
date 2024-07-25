package com.example.exams.Controllers;

import com.example.exams.Model.Data.ProperDataModels.ProblemCategories;
import com.example.exams.Model.Data.ProperDataModels.ProblemStatus;
import com.example.exams.Model.Data.ProperDataModels.ShowProblem;
import com.example.exams.Model.Data.db.Problem;
import com.example.exams.Services.ExaminerService;
import com.example.exams.Services.ProblemService;
import com.example.exams.SpringSecurity.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import java.io.IOException;
import java.util.*;

@Controller
public class ProblemController {

    ProblemService problemService;

    ExaminerService examinerService;

    @Autowired
    ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Autowired
    void ExaminerService(ExaminerService examinerService) {
        this.examinerService = examinerService;
    }


    @GetMapping("/reportProblem")
    public ModelAndView reportProblem() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("problemReporting");
        List<String> categories = problemService.getUniqueCategories();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @PostMapping("/addProblem")
    public String addProblem(@RequestParam("category") ProblemCategories category,
                             @RequestParam("description") String description,
                             @RequestParam("image") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            return "redirect:/logged";
        }
        String contentType = imageFile.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            return "redirect:/logged";
        }
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            return "redirect:/logged";
        }


        Problem problem = new Problem();
        problem.setDescription(description);
        problem.setCategory(category);
        byte[] image = imageFile.getBytes();
        problem.setPhoto(image);

        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }
        assert user != null;
        String userName = user.getUsername();
        problem.setUsername(userName);
        problem.setProblemsExaminer(examinerService.findByLogin(user.getUsername()));

        problemService.AddOne(problem);
        return "redirect:/exams";
    }

    @GetMapping("/showProblems")
    public ModelAndView showProblems(@RequestParam(required = false) String category, @RequestParam(required = false) String status) {
        ModelAndView modelAndView = new ModelAndView("showProblems");

        List<Problem> problems = problemService.GetAll();
        Map<String, List<ShowProblem>> problemsByCategory = new HashMap<>();

        for (Problem problem : problems) {
            byte[] photoBytes = problem.getPhoto();
            String base64Encoded = Base64.getEncoder().encodeToString(photoBytes);

            ShowProblem showProblem = new ShowProblem();
            showProblem.setDescription(problem.getDescription());
            showProblem.setPhoto(base64Encoded);
            showProblem.setCategory(problem.getCategory().toString());
            showProblem.setUsername(problem.getUsername());
            showProblem.setExaminer(problem.getProblemsExaminer());
            showProblem.setId(problem.getId());
            showProblem.setStatus(problem.getStatus().toString());

            problemsByCategory.computeIfAbsent(problem.getCategory().toString(), k -> new ArrayList<>()).add(showProblem);
        }



        Map<String, List<ShowProblem>> selectedProblemsByCategory = null;
        if(status!=null && category!=null)
        {
            selectedProblemsByCategory = problemService.getProblemsByCategoryAndStatus(problemsByCategory,category,status);
            modelAndView.addObject("problemsByCategory", selectedProblemsByCategory);
        }
        else {
            modelAndView.addObject("problemsByCategory", problemsByCategory);
        }

        List<String> categories = problemService.getUniqueCategories();
        modelAndView.addObject("categories", categories);

        List<String> statuses = problemService.getUniqueStatuses();
        modelAndView.addObject("statuses", statuses);

        return modelAndView;
    }

    @GetMapping("/showProblems/{id}")
    public ModelAndView problemDetails(@PathVariable("id") Integer problemId) {
        ModelAndView modelAndView = new ModelAndView("problemDetails");
        Problem problem = problemService.findById(problemId);
        byte[] photoBytes = problem.getPhoto();
        String photo = Base64.getEncoder().encodeToString(photoBytes);
        modelAndView.addObject("problem", problem);
        modelAndView.addObject("problemStatuses", problemService.getUniqueStatuses());
        modelAndView.addObject("photo", photo);
        return modelAndView;
    }

    @PostMapping("/changeProblemStatus")
    public String changeProblemStatus(@RequestParam("id") Integer id,
                                      @RequestParam("status") ProblemStatus status) {
        problemService.changeStatus(id,status);
        return "redirect:/showProblems";
    }

}
