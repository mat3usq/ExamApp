package com.example.exams.Controllers;


import com.example.exams.Model.Data.ProperDataModels.ExamDTO;
import com.example.exams.Model.Data.db.*;
import com.example.exams.Repositories.Db.*;
import com.example.exams.Services.*;
import com.example.exams.SpringSecurity.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import com.example.exams.Model.Data.ProperDataModels.ExamResponseDTO;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.standard.expression.Each;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Controller
public class ExamController {

    @Autowired
    ExamService examService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    ExaminerService examinerService;

    @Autowired
    OpenQuestionService openQuestionService;

    @Autowired
    ClosedQuestionService closedQuestionService;

    @Autowired
    LogstudentexamService logstudentexamService;

    @Autowired
    private AnswerClosedService answerClosedService;

    @Autowired
    private AnswerOpenService answerOpenService;

    @Autowired
    private StudentClosedAnswerService studentClosedAnswerService;

    @Autowired
    private StudentOpenAnswerService studentOpenAnswerService;


    @Autowired
    private UsersService usersService;

    @Autowired
    private LogsService logsService;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private StudentsService studentsService;

    // private final NotificationService notificationService;

    // @Autowired
    // public ExamController(NotificationService notificationService) {
    //     this.notificationService = notificationService;
    // }

    @PostMapping("/addExamQuestions")
    public String addExamQuestions(@RequestBody String body, Authentication authentication) {

        String string = body.toString();
        try {
            String[] pairs = string.split("&");
            Map<String, String> map = new HashMap<>();
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    map.put(keyValue[0], keyValue[1]);
                }
            }

            String examDataString = map.get("examData");

            try {
                String decodedString = URLDecoder.decode(examDataString, "UTF-8");
                ObjectMapper objectMapper = new ObjectMapper().registerModule(new ParameterNamesModule())
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule());
                ExamDTO exam = objectMapper.readValue(decodedString, ExamDTO.class);
                exam.setEgzamiantor(examinerService.findByLogin(authentication.getName()).getExaminer_id());

                UserDetails user = null;
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    user = (UserDetails) session.getAttribute("UserDetails");
                }
                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
                boolean isExaminer = false;
                for (GrantedAuthority authority : authorities) {
                    if ("ROLE_EXAMINER".equals(authority.getAuthority())) {
                        Examiner examiner = usersService.getExaminerByLoginAndPassword(user.getUsername());
                        logsService.addLog(new Log("Egzaminator: " + examiner.getFirstname() + " " + examiner.getLastname() + " dodał nowy egzamin o id: " + examService.getNextExamId() + " oraz opisie: " + exam.getDescription()));
                        examService.AddExam(exam);
                        break;
                    }
                    if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                        Administrator administrator = usersService.getAdministratorByLogin(user.getUsername());
                        logsService.addLog(new Log("Administrator: " + administrator.getFirstname() + " " + administrator.getLastname() + " dodał nowy egzamin o id: " + examService.getNextExamId() + " oraz opisie: " + exam.getDescription()));
                        examService.AddExam(exam);
                        break;
                    }
                }
                return "redirect:/exams";
            } catch (UnsupportedEncodingException e) {
                System.out.println("Błąd dekodowania URL: " + e.getMessage());
            }

            return "redirect:/exams";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error-page";
        }
    }

//    @PostMapping("/addExam/{egzaminator_egzaminator_id}")
//    public String UpdateOpenQuestion(@ModelAttribute Exam exam, @RequestParam Integer subjectid, Authentication authentication) {
//        exam.setQuestionPoolStrategy(false);
//        exam.setQuestionPool(0);
//        Examiner examiner = examinerService.findByLogin(authentication.getName());
//        exam.setConductingExaminer(examiner);
//        exam.setExamsSubject(subjectService.Get(subjectid));
//        Exam addedExam = examService.AddExam(exam);
//        return "redirect:/exams";
//    }

    @PostMapping("/editExamDetails/{examId}")
    public String editExamDetails(@PathVariable Integer examId, @RequestParam("exampoolstrategy") Boolean exampoolstrategy, @RequestParam("count") Integer exampool, @ModelAttribute Exam exam, @RequestParam("subject") Integer subjectId, @RequestParam(value = "startdate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam(value = "starttime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime, @RequestParam(value = "enddate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate, @RequestParam(value = "endtime", required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime) {
        String desc = exam.getDescription();
        exam = examService.GetExam(examId.intValue());
        exam.setStartDate(startDate);
        exam.setStartTime(startTime);
        exam.setEndDate(endDate);
        exam.setEndTime(endTime);
        Subject subject = subjectService.getSubjectById(subjectId.intValue());
        exam.setId(examId);
        exam.setExamsSubject(subject);
        exam.setDescription(desc);
        String s = examService.getExamChange(examService.GetExam(examId.intValue()), exam);
        exam.setDuration(Duration.between(LocalDateTime.of(exam.getStartDate(), exam.getStartTime()), LocalDateTime.of(exam.getEndDate(), exam.getEndTime())).toMinutes());

        if (s != null) {
            logsService.updateExam(exam, s);
            examService.updateExam(exam);
        }
        examService.changeExamPoolStrategy(exampoolstrategy, exam);
        examService.setExamPool(exampool, exam);
        return "redirect:/exams";
    }

    @GetMapping("/editExam/{examId}")
    public ModelAndView editExam(@PathVariable Integer examId, Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        Exam exam = examService.GetExam(examId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editExam");
        modelAndView.addObject("subjects", subjects);
        modelAndView.addObject("exam", exam);
        model.addAttribute("examId", examId);
        model.addAttribute("questionQuantity", examService.getQuestionsQuantity(examId));
        model.addAttribute("questionPool", exam.getQuestionPool());
        model.addAttribute("questionPoolStrategy", exam.getQuestionPoolStrategy() ? 1 : 0);
        return modelAndView;
    }

    @GetMapping("/setExamPoolStrategy/{examId}")
    public String setExamPoolStrategy(@PathVariable Integer examId, @RequestParam Boolean strategy) {
        examService.changeExamPoolStrategy(strategy, examService.GetExam(examId));
        return "redirect:/exams";
    }

    @GetMapping("/setExamPoolStrategyTrue/{examId}/{count}")
    public String setExamPoolStrategyTrue(@PathVariable Integer examId, @PathVariable Integer count) {
        examService.changeExamPoolStrategy(true, examService.GetExam(examId));
        int pool = examService.setExamPool(count, examService.GetExam(examId));
        return "redirect:/exams";
    }

    @PostMapping("/setExamPoolStrategyFalse/{examId}")
    public String setExamPoolStrategyFalse(@PathVariable Integer examId) {
        examService.changeExamPoolStrategy(false, examService.GetExam(examId));
        return "redirect:/exams";
    }

    @GetMapping("/showDoneExamUser/{examId}")
    public ModelAndView
    showDoneExamUser(@PathVariable Integer examId, Model model) {
        Exam exam = examService.GetExam(examId.intValue());
        List<Student> studentopenAnswers = answerOpenService.getAllDistinctStudentsForOpenQuestions(examId.intValue());
        List<Logstudentexam> list = logstudentexamService.getStudentsLogstudentExamById(exam);
        List<OpenQuestion> openQuestions = openQuestionService.getAllByExamId(examId);
        List<Closedquestion> closedquestions = closedQuestionService.getAllByExamId(examId);

        HashMap<Student, List<Studentopenanswer>> map = new HashMap<>();

        for (int i = 0; i < studentopenAnswers.size(); i++) {
            map.put(studentopenAnswers.get(i), answerOpenService.getStudentOpenAnswerByStudent(studentopenAnswers.get(i)));
        }
        HashMap<Integer, LocalTime> mapTime = new HashMap<>();
        HashMap<Integer, LocalDate> mapDate = new HashMap<>();
        for (Map.Entry<Student, List<Studentopenanswer>> entry : map.entrySet()) {
            Student student = entry.getKey();

            LocalDate date = entry.getValue().get(0).getDate();
            LocalTime time = entry.getValue().get(0).getTime();
            mapTime.put(student.getStudentId(), time);
            mapDate.put(student.getStudentId(), date);
        }
        int points = 0;
        for (int i = 0; i < openQuestions.size(); i++) {
            points += openQuestions.get(i).getScore();
        }
        for (int i = 0; i < closedquestions.size(); i++) {
            points += closedquestions.get(i).getScore();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showDoneExamUsers");
        modelAndView.addObject("exam", exam);
        modelAndView.addObject("Students", studentopenAnswers);
        modelAndView.addObject("mapDate", mapDate);
        modelAndView.addObject("mapTime", mapTime);
        modelAndView.addObject("list", list);
        model.addAttribute("examId", examId);
        model.addAttribute("points", points);
        return modelAndView;
    }

    @PostMapping("/rate")
    public ModelAndView rateStudent(@RequestParam Integer examId, @RequestParam Integer studentId, Model model) {
        Exam exam = examService.GetExam(examId);
        Student student = usersService.getStudentByid(studentId);
        List<Studentopenanswer> answerOpen = answerOpenService.getStudentOpenAnswerByStudent(student);
        List<OpenQuestion> openQuestions = openQuestionService.getAllByExamId(exam.getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("evaluateExam");
        modelAndView.addObject("student", student);
        modelAndView.addObject("exam", exam);
        modelAndView.addObject("listOpenQuestions", openQuestions);
        modelAndView.addObject("listAnswerOpenQuestion", answerOpen);
        return modelAndView;
    }

    @GetMapping("/evaluateExam")
    public String evaluateExam(@RequestParam("studentId") Integer studentId, @RequestParam("examId") Integer examId, @RequestParam List<Integer> scores, @RequestParam("examinerComment") String examinerComment, String to) {
        Student student = usersService.getStudentByid(studentId.intValue());
        Exam exam = examService.GetExam(examId.intValue());
        logstudentexamService.setDateTimeExaminerComment(student, exam, examinerComment);
        int points = answerOpenService.updateScores(student, scores);
        logstudentexamService.addOpenPoints(student, exam, points);

        String sendTo = student.getEmail();
        String subject = "Wyniki egzaminu";
        String text = String.format("Twój egzamin '%s' został oceniony. Uzyskałeś %d punktów. Komentarz egzaminatora: %s", exam.getDescription(), points, examinerComment);
        //notificationService.sendNotification(sendTo, subject, text);
        return "redirect:/exams";
    }

    @GetMapping("/confirmExamDeletion/{examId}")
    public ModelAndView deleteExam(@PathVariable Integer examId, Model model) {
//        boolean deleted = examService.deleteExam(examId);
//        if (deleted) {
//            return ResponseEntity.ok("Exam deleted successfully");
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exam not found");
        Exam exam = examService.GetExam(examId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("deleteExam");
        modelAndView.addObject("exam", exam);
        model.addAttribute("examId", examId);
//        examService.deleteExam(examId);
        return modelAndView;
    }

    @PostMapping("/deleteExam/{examId}")
    public String deleteExam(@RequestParam Integer examId) {
        logsService.deleteExam(examId.intValue());
        openQuestionService.deleteAllOpenQuestionsByExamId(examId.intValue());
        closedQuestionService.deleteAllClosedQuestionsByExamId(examId.intValue());
        logstudentexamService.deleteAllLogsForExam(examService.GetExam(examId.intValue()));
        boolean deleted = examService.deleteExam(examId);

        return "redirect:/exams";
    }

    @GetMapping("/showExamDetails/{examId}")
    public ModelAndView getExamDetails(@PathVariable String examId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("examDetails");

        Exam exam = examService.GetExam(Integer.parseInt(examId));

        List<Closedquestion> closedquestions = closedQuestionService.getAllByExamId(Integer.parseInt(examId));
        Map<Integer, List<Answerclosed>> closedQuestionAnswersMap = new HashMap<>();
        for (Closedquestion closedQuestion : closedquestions) {
            List<Answerclosed> answers = answerClosedService.getAllByQuestionId(closedQuestion.getId());
            closedQuestionAnswersMap.put(closedQuestion.getId(), answers);
        }
        modelAndView.addObject("exam", examService.GetExam(Integer.parseInt(examId)));
        modelAndView.addObject("listOpenQuestions", openQuestionService.getAllByExamId(Integer.parseInt(examId)));
        modelAndView.addObject("countOpenQuestions", openQuestionService.getAllByExamId(Integer.parseInt(examId)).size());
        modelAndView.addObject("listClosedQuestions", closedquestions);
        modelAndView.addObject("closedQuestionAnswersMap", closedQuestionAnswersMap);

        return modelAndView;
    }

    @GetMapping("/solveExam/{examId}")
    public ModelAndView getExamToSolve(@PathVariable String examId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("solveExam");

        Exam exam = examService.GetExam(Integer.parseInt(examId));
        LocalDate startDate = exam.getStartDate();
        LocalTime startTime = exam.getStartTime();
        LocalDate endDate = exam.getEndDate();
        LocalTime endTime = exam.getEndTime();


        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }

        if (user == null) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("message", "You must be logged in to access this page.");
            return modelAndView;
        }

        Student student = studentsService.getStudentByLogin(user.getUsername());
        if (logstudentexamService.existsByExamIdAndStudentId(Integer.parseInt(examId), student.getStudentId())) {
            modelAndView.setViewName("error/404");
            modelAndView.addObject("message", "You have already taken this exam.");
            return modelAndView;
        }


        if (LocalDate.now().isAfter(startDate) || LocalDate.now().equals(startDate)) {
            if (LocalTime.now().isAfter(startTime) || LocalTime.now().equals(startTime))
                modelAndView.addObject("timeStarts", true);
            else
                modelAndView.addObject("timeStarts", false);
        } else
            modelAndView.addObject("timeStarts", false);

        if (LocalDate.now().isBefore(endDate) || LocalDate.now().equals(endDate)) {
            if (LocalTime.now().isBefore(endTime) || LocalTime.now().equals(endTime))
                modelAndView.addObject("timeEnds", false);
            else
                modelAndView.addObject("timeEnds", true);
        } else
            modelAndView.addObject("timeEnds", true);

        List<OpenQuestion> openQuestions = openQuestionService.getAllByExamId(Integer.parseInt(examId));
        List<Closedquestion> closedQuestions = closedQuestionService.getAllByExamId(Integer.parseInt(examId));

        int open_Question_Size = openQuestionService.getAllByExamId(Integer.parseInt(examId)).size();
        int close_Question_Size = closedQuestionService.getAllByExamId(Integer.parseInt(examId)).size();

        List<Integer> randomIndexes = generateRandomNumbers(open_Question_Size + close_Question_Size, exam.getQuestionPool());
        List<Integer> openQuestionIndexes = selectNumbersInRange(randomIndexes, 0, open_Question_Size - 1);

        List<Integer> closedQuestionIndexes = selectNumbersInRange(randomIndexes, open_Question_Size, open_Question_Size + close_Question_Size - 1);
        for (int i = 0; i < closedQuestionIndexes.size(); i++) {
            closedQuestionIndexes.set(i, closedQuestionIndexes.get(i) - open_Question_Size);
        }

        List<OpenQuestion> randomOpenQuestions = new ArrayList<>();
        List<Closedquestion> randomClosedQuestions = new ArrayList<>();

        for (int index : openQuestionIndexes) {
            randomOpenQuestions.add(openQuestions.get(index));
        }
        for (int index : closedQuestionIndexes) {
            randomClosedQuestions.add(closedQuestions.get(index));
        }

        modelAndView.addObject("exam", examService.GetExam(Integer.parseInt(examId)));
        //modelAndView.addObject("listOpenQuestions", openQuestionService.getAllByExamId(Integer.parseInt(examId)));
        modelAndView.addObject("listOpenQuestions", randomOpenQuestions);


       // List<Closedquestion> listClosedQuestions = closedQuestionService.getAllByExamId(Integer.parseInt(examId));
        List<List<Answerclosed>> closedAnswers = new ArrayList<>();


        for (int i = 0; i < randomClosedQuestions.size(); i++)
            closedAnswers.add(answerClosedService.getAllByQuestionId(randomClosedQuestions.get(i).getId()));

        //modelAndView.addObject("listClosedQuestions", closedQuestionService.getAllByExamId(Integer.parseInt(examId)));
        modelAndView.addObject("listClosedQuestions", randomClosedQuestions);

        modelAndView.addObject("closedAnswers", closedAnswers);

        return modelAndView;
    }

    public static List<Integer> generateRandomNumbers(int x, int t) {
        List<Integer> selectedNumbers = new ArrayList<>();
        Random random = new Random();

        while (selectedNumbers.size() < t) {
            int randomNumber = random.nextInt(x);
            if (!selectedNumbers.contains(randomNumber)) {
                selectedNumbers.add(randomNumber);
            }
        }

        return selectedNumbers;
    }

    public static List<Integer> selectNumbersInRange(List<Integer> numbers, int a, int b) {
        List<Integer> selectedNumbersInRange = new ArrayList<>();

        for (int number : numbers) {
            if (number >= a && number <= b) {
                selectedNumbersInRange.add(number);
            }
        }

        return selectedNumbersInRange;
    }

    private boolean processOpenAnswers(Map<String, String> openAnswers, UserDetails userDetails) {
        Student student = studentsService.getStudentByLogin(userDetails.getUsername());

        if (openAnswers == null || openAnswers.isEmpty()) {
            return true;
        }

        openAnswers.forEach((questionId, answer) -> {
            Studentopenanswer openAnswer = new Studentopenanswer();
            OpenQuestion openquestion = openQuestionService.getOpenQuestionById(questionId);
            if (openquestion != null && student != null) {
                openAnswer.setOpenquestionQuestionid(openquestion);
                openAnswer.setDescription(answer);
                openAnswer.setStudentStudent(student);
                openAnswer.setDate(LocalDate.now());
                openAnswer.setTime(LocalTime.now());
                studentOpenAnswerService.saveStudentOpenAnswer(openAnswer);
            }
        });
        return true;

    }

    private void resultsClosedAnswers(Integer score, int id, Integer questionid) {
        int result = 0;
        //  System.out.println(score);
        List<Answerclosed> Answers = answerClosedService.getAllByQuestionId(questionid);
        for (Answerclosed answer : Answers) {

            if (answer.isCorrect()) {
                //   System.out.println(answer.getId() + " DObre Answerid");
                result = result + 1;
            } else {

                //     System.out.println(answer.getId() + "Zle Answerid");
            }
        }
        //     System.out.println(result);
        if (result == score) {
            logstudentexamService.addPointsToLogstudentexam(id, 1);
        } else {
            logstudentexamService.addPointsToLogstudentexam(id, 0);
        }
    }

    private boolean processClosedAnswers(Map<String, String[]> closedAnswers, UserDetails userDetails, Integer currentlogsstudentexam) {
        Student student = studentsService.getStudentByLogin(userDetails.getUsername());
        if (student == null) {
            return false;
        }
        if (closedAnswers == null || closedAnswers.isEmpty()) {
            return true;
        }


        for (Map.Entry<String, String[]> entry : closedAnswers.entrySet()) {
            Integer questionId = Integer.parseInt(entry.getKey());
            Closedquestion closedQuestion = closedQuestionService.getClosedQuestionById(questionId);
            Integer questionresult = 0;
            for (String answerId : entry.getValue()) {
                Answerclosed answerclosed = answerClosedService.getAnswerClosedById(answerId);
                if (answerclosed == null) {
                    return false;
                }
                Studentclosedanswer closedAnswer = new Studentclosedanswer();
                closedAnswer.setAnswerclosedAnswerid(answerclosed);
                closedAnswer.setClosedquestionQuestionid(closedQuestion);
                closedAnswer.setStudentStudent(student);
                closedAnswer.setDate(LocalDate.now());
                boolean isCorrect = answerclosed.isCorrect();
                if (isCorrect) {
                    questionresult += 1;
                } else {
                    questionresult -= 1;
                }
                closedAnswer.setCorrectness(isCorrect);
                studentClosedAnswerService.saveStudentClosedAnswer(closedAnswer);
            }
            resultsClosedAnswers(questionresult, currentlogsstudentexam, questionId);
        }

        return true;
    }

    @PostMapping("/saveResolvedExam")
    public String saveExam(@ModelAttribute ExamResponseDTO examResponse) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showExams");
        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            modelAndView.addObject("UsersEntity", session.getAttribute("UserDetails"));
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }

        if (session.getAttribute(user.getUsername() + "_solved_" + examResponse.getExamId()) == null) {
            Servicestatistic servicestatistic = logsService.getServiceStatistic();
            int examCount = servicestatistic.getExamscount() + 1;
            servicestatistic.setExamscount(examCount);
            logsService.updateServicestatistic(servicestatistic);
            session.setAttribute(user.getUsername() + "solved_" + examResponse.getExamId(), true);
        }

        Student student = studentsService.getStudentByLogin(user.getUsername());
        Exam currentExam = examService.GetExam(examResponse.getExamId());
        Integer logstudentexamid = logstudentexamService.createAndSaveLogstudentexam(currentExam, student);
        boolean openAnswersSaved = processOpenAnswers(examResponse.getOpenAnswers(), user);
        boolean closedAnswersSaved = processClosedAnswers(examResponse.getClosedAnswers(), user, logstudentexamid);
        if (!openAnswersSaved || !closedAnswersSaved) {
            modelAndView.addObject("error", "Nie udało się zapisać wszystkich odpowiedzi.");
        }

        return "redirect:/exams";
    }


    @GetMapping("/addQuestion/{examId}")
    public ModelAndView addQuestion(@PathVariable Integer examId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("closedQuestion", new Closedquestion());
        modelAndView.addObject("answerClosed", new Answerclosed());
        modelAndView.addObject("openQuestion", new OpenQuestion());
        modelAndView.setViewName("addQuestion");
        return modelAndView;
    }

    @PostMapping("/addQuestion/{examId}")
    public String addQuestion(@Valid @ModelAttribute OpenQuestion openQuestion, BindingResult result,
                              @ModelAttribute Closedquestion closedquestion,
                              @RequestParam(required = false) List<String> closedanswer,
                              @RequestParam(required = false) List<String> correctness,
                              @RequestParam(value = "type", required = false) String type,
                              @PathVariable Integer examId, Model model) {
        if (result.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            result.getAllErrors().forEach(error -> {
                if (error instanceof FieldError fieldError) {
                    String field = fieldError.getField();
                    String defaultMessage = fieldError.getDefaultMessage();
                    fieldErrors.put(field, defaultMessage);
                }
            });
            model.addAttribute("fieldErrors", fieldErrors);
            return "addQuestion";
        }

        Exam exam = examService.GetExam(examId);
        if ("closed".equals(type)) {
            logsService.addClosedQuestionToExam(examId.intValue(), closedquestion);
            closedquestion.setExam(exam);
            closedQuestionService.addClosedQuestion(closedquestion);
            for (int i = 0; i < closedanswer.size(); i++) {
                String answer = closedanswer.get(i);
                Answerclosed answerclosed = new Answerclosed();
                Boolean correct = correctness.get(i).equals("on");
                answerclosed.setCorrect(correct);
                answerclosed.setDescription(answer);
                answerClosedService.addAnswerClosed(answerclosed, closedquestion);
            }

        } else {
            logsService.addOpenQuestionToExam(examId.intValue(), openQuestion);
            openQuestion.setExam(exam);
            openQuestionService.AddOpenQuestion(openQuestion);
        }
        return "redirect:/exams";
    }

    @GetMapping("/addStudents/{examId}")
    public ModelAndView addStudents(@PathVariable Integer examId) {
        ModelAndView modelAndView = new ModelAndView();

        Exam exam = this.examService.GetExam(examId);

        List<Student> addedStudents = exam.getStudents();
        List<Group> groups = groupsService.getAllGroups();
        List<Student> allStudents = studentsService.getAllStudents();

        List<Student> availableStudents = allStudents.stream()
                .filter(student -> !addedStudents.contains(student))
                .collect(Collectors.toList());

        modelAndView.addObject("groups", groups);
        modelAndView.addObject("students", availableStudents);
        modelAndView.addObject("addedStudents", addedStudents);

        modelAndView.setViewName("addStudents");
        return modelAndView;
    }

    @PostMapping("/addSingleStudent")
    public String addSingleStudent(@RequestParam("examId") String examId, @RequestParam("studentId") Integer studentId) {
        try {
            int parsedExamId = Integer.parseInt(examId);
            System.out.println("addSingleStudent - examId: " + parsedExamId + ", studentId: " + studentId);
            Exam exam = this.examService.GetExam(parsedExamId);
            Student student = this.studentsService.getStudentById(studentId);

            if (!exam.getStudents().contains(student)) {
                exam.getStudents().add(student);
                this.examService.updateExam(exam);
            }

            return "redirect:/addStudents/" + parsedExamId;
        } catch (NumberFormatException e) {
            System.err.println("Błąd parsowania examId: " + examId);
            return "redirect:/exams";
        }
    }

    @PostMapping("/addStudentsFromGroup")
    public String addStudentsFromGroup(@RequestParam("examId") String examId, @RequestParam("groupId") Integer groupId) {
        try {
            int parsedExamId = Integer.parseInt(examId);
            System.out.println("addStudentsFromGroup - examId: " + parsedExamId + ", groupId: " + groupId);
            Exam exam = this.examService.GetExam(parsedExamId);
            Group group = this.groupsService.getGroupByGroupId(groupId);

            List<Student> studentsToAdd = group.getStudents().stream()
                    .filter(student -> !exam.getStudents().contains(student))
                    .toList();

            exam.getStudents().addAll(studentsToAdd);
            this.examService.updateExam(exam);
            return "redirect:/addStudents/" + parsedExamId;
        } catch (NumberFormatException e) {
            System.err.println("Błąd parsowania examId: " + examId);
            return "redirect:/exams";
        }
    }


    @PostMapping("/processForm")
    public String processForm(@RequestParam("action") String action, RedirectAttributes redirectAttributes) {

        CustomUserDetails user = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            user = (CustomUserDetails) session.getAttribute("UserDetails");
        }

        Integer examId = Integer.parseInt(action.substring(action.indexOf(':') + 1));

        if (action.startsWith("solveExam:")) {
            if (examService.hasUserAlreadySolvedExam(user.getUserId(), examId)) {
                return "redirect:/solveExam/" + examId;
            } else {
                return "redirect:/exams";
            }
        }

        if (action.startsWith("show:")) {
            return "redirect:/showExamDetails/" + examId;
        } else if (action.startsWith("edit:")) {
            return "redirect:/editExam/" + examId;
        } else if (action.startsWith("delete:")) {
            return "redirect:/confirmExamDeletion/" + examId;
        } else if (action.startsWith("showDoneExamUser:")) {
            return "redirect:/showDoneExamUser/" + examId;
        } else if (action.startsWith("addQuestion:")) {
            return "redirect:/addQuestion/" + examId;
        } else if (action.startsWith("addStudents:")) {
            return "redirect:/addStudents/" + examId;
        } else if (action.startsWith("toggleVisibility:")) {
            return "redirect:/toggleVisibility/" + examId;
        } else if (action.startsWith("results:")) {
            return "redirect:/exam/" + examId + "/grades";
        }
        return "error/404";
    }

    @GetMapping("/toggleVisibility/{examId}")
    public String toggleExamVisibility(@PathVariable Integer examId) {
        examService.changeExamVisibility(examId);
        return "redirect:/exams";
    }
}
