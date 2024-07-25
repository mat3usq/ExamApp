package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.OpenQuestion;
import com.example.exams.Services.LogsService;
import com.example.exams.Services.OpenQuestionService;
import com.example.exams.Services.StudentOpenAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OpenQuestionController {
//    private final OpenQuestionService openQuestionService;
//
//    @Autowired
//    OpenQuestionController(OpenQuestionService openQuestionService){
//        this.openQuestionService = openQuestionService;
//    }

    @Autowired
    OpenQuestionService openQuestionService;

    @Autowired
    StudentOpenAnswerService studentOpenAnswerService;
    @Autowired
    LogsService logsService;

    @GetMapping("/editOpenQuestion/{openQuestionId}")
    public ModelAndView EditOpenQuestion(@PathVariable Integer openQuestionId){
        OpenQuestion openQuestion = openQuestionService.GetOpenQuestion(openQuestionId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editOpenQuestion");
        modelAndView.addObject("openQuestion", openQuestion);
        return modelAndView;
    }

    @PostMapping("/updateOpenQuestion/{openQuestionId}")
    public String UpdateOpenQuestion(@ModelAttribute OpenQuestion openQuestion, @PathVariable Integer openQuestionId){
        logsService.updateOpenQuestion(openQuestion,openQuestionId);
        openQuestion.setOpenQuestionId(openQuestionId);
        OpenQuestion updatedOpenQuestionOptional = openQuestionService.UpdateOpenQuestion(openQuestion);
        return "redirect:/showExamDetails/" + updatedOpenQuestionOptional.getExam().getId();
    }

    @GetMapping("/createOpenQuestion")
    public ModelAndView createQuestion() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("openQuestion", new OpenQuestion());
        modelAndView.setViewName("createOpenQuestion");
        return modelAndView;
    }

    @PostMapping("/createOpenQuestion")
    public void add(@ModelAttribute OpenQuestion openquestion)
    {
        openQuestionService.AddOpenQuestion(openquestion);
    }


    @GetMapping("/openQuestions")
    public ModelAndView getAllOpenQuestions() {
        ModelAndView modelAndView = new ModelAndView("openQuestions/list");
        modelAndView.addObject("questions", openQuestionService.getAll());
        modelAndView.setViewName("questionsView");
        return modelAndView;
    }

    @GetMapping("/deleteOpenQuestion/{openQuestionId}")
    public ResponseEntity<String> deleteOpenQuestion(@PathVariable Integer openQuestionId) {
        boolean deleted = openQuestionService.deleteOpenQuestion(openQuestionId);
        if (deleted) {
            return ResponseEntity.ok("Question deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found");
    }

    @PostMapping("/processOpenQuestionForm")
    public String processOpenQuestionForm(@RequestParam("action") String action) {
        String[] parts = action.split(":");
        String command = parts[0];
        Integer openQuestionId = Integer.parseInt(parts[1]);
        if ("edit".equals(command)) {
            return "redirect:/editOpenQuestion/" + openQuestionId;
        } else if ("delete".equals(command) && parts.length == 3) {
            Integer examId = Integer.parseInt(parts[2]);
            studentOpenAnswerService.deleteAllAnswersByQuestionId(openQuestionId);
            logsService.deleteOpenQuestion(openQuestionId.intValue());
            openQuestionService.deleteOpenQuestion(openQuestionId);
            return "redirect:/showExamDetails/" + examId;
        }
        return "error";

    }

}
