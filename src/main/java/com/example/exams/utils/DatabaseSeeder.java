package com.example.exams.utils;

import com.example.exams.Model.Data.ProperDataModels.ProblemCategories;
import com.example.exams.Model.Data.ProperDataModels.ProblemStatus;
import com.example.exams.Model.Data.db.*;
import com.example.exams.Repositories.Db.*;
import com.example.exams.Services.GroupsService;
import com.example.exams.Services.LogstudentexamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final AdministratorsEntityRepository administratorRepository;
    private final SubjectRepository subjectRepository;
    private final ExaminerRepository examinerRepository;
    private final ExamRepository examRepository;
    private final OpenQuestionRepository openQuestionRepository;
    private final ClosedQuestionRepository closedQuestionRepository;
    private final AnswerClosedRepository answerClosedRepository;

    private final StudentsEntityRepository studentsRepository;
    private final ProblemRepository problemRepository;
    private final GroupEntityRepository groupRepository;
    private final ServicestatisticRepository servicestatisticRepository;
    private final LogstudentexamRepository logstudentexamRepository;
    private final LogstudentexamService logstudentexamService;

    public DatabaseSeeder(AdministratorsEntityRepository administratorRepository, ExamRepository examRepository, SubjectRepository subjectRepository, ExaminerRepository examinersRepository, OpenQuestionRepository openQuestionRepository, ClosedQuestionRepository closedQuestionRepository, AnswerClosedRepository answerClosedRepository, StudentsEntityRepository studentsEntityRepository, ProblemRepository problemRepository, GroupEntityRepository groupRepository, ServicestatisticRepository servicestatisticRepository, LogstudentexamRepository logstudentexamRepository, LogstudentexamService logstudentexamService) {
        this.administratorRepository = administratorRepository;
        this.examinerRepository = examinersRepository;
        this.examRepository = examRepository;
        this.subjectRepository = subjectRepository;
        this.openQuestionRepository = openQuestionRepository;
        this.closedQuestionRepository = closedQuestionRepository;
        this.answerClosedRepository = answerClosedRepository;
        this.studentsRepository = studentsEntityRepository;
        this.problemRepository = problemRepository;
        this.groupRepository = groupRepository;
        this.servicestatisticRepository = servicestatisticRepository;
        this.logstudentexamRepository = logstudentexamRepository;
        this.logstudentexamService = logstudentexamService;
    }

    @Override
    public void run(String... args) throws Exception {
        administratorRepository.save(new Administrator(1, "Krzysztof", "Konon", "wipb", "pebegate", "wi@pb.edu.pl", true));
        administratorRepository.save(new Administrator(2, "Wojciech", "Walerian", "wojtek", "1", "wojtek@pb.edu.pl", true));

        subjectRepository.save(new Subject(1, "Matematyka"));
        subjectRepository.save(new Subject(2, "Angielski"));
        subjectRepository.save(new Subject(3, "Polski"));
        subjectRepository.save(new Subject(4, "Historia"));
        subjectRepository.save(new Subject(5, "Niemiecki"));

        Examiner examiner1 = new Examiner(1, "Slawomir", "Golibroda", "z", "z", "s.golibrodai@pb.edu.pl", true);
        Examiner examiner2 = new Examiner(2, "Dorota", "Warka", "warka", "warka", "wedliny@pb.edu.pl", true);
        Examiner examiner3 = new Examiner(3, "Julita", "Komarewska", "enjoyer", "szastprast", "smok@pb.edu.pl", false);
        examinerRepository.save(examiner1);
        examinerRepository.save(examiner2);
        examinerRepository.save(examiner3);

        List<Student> students1 = new ArrayList<>();
        List<Student> students2 = new ArrayList<>();

        Student student1 = new Student(1, "Anna", "Mosiezna", "a", "a", "a@student.pb.edu.pl");
        Student student2 = new Student(2, "Bartosz", "Balagan", "balagan", "balagan", "b@student.pb.edu.pl");
        Student student3 = new Student(3, "Michal", "Cwelarczyk", "cwelarczyk", "cwelarczyk", "c@student.pb.edu.pl");
        Student student4 = new Student(4, "Danusia", "Dotkowska", "dotkowska", "dotkowska", "d@student.pb.edu.pl");
        Student student5 = new Student(5, "Eleonora", "Embark", "embark", "embark", "e@student.pb.edu.pl");
        Student student6 = new Student(6, "Florencja", "Figurska", "figurska", "figurska", "f@student.pb.edu.pl");
        Student student7 = new Student(7, "Gabriela", "Ciemnik", "ciemnik", "ciemnik", "g@student.pb.edu.pl");

        students1.add(student1);
        students1.add(student2);
        students1.add(student3);

        students2.add(student4);
        students2.add(student5);
        students2.add(student6);

        studentsRepository.save(student1);
        studentsRepository.save(student2);
        studentsRepository.save(student3);
        studentsRepository.save(student4);
        studentsRepository.save(student5);
        studentsRepository.save(student6);
        studentsRepository.save(student7);

        groupRepository.save(new Group(1, "AAA", students1));
        groupRepository.save(new Group(2, "BBB", students2));

        groupRepository.save(new Group(3, "pb01", new ArrayList<>()));
        groupRepository.save(new Group(4, "pb02", new ArrayList<>()));
        groupRepository.save(new Group(5, "pb03", new ArrayList<>()));

        examinerRepository.save(new Examiner(1, "Slawomir", "Golibroda", "z", "z", "s.golibrodai@pb.edu.pl", true));
        examinerRepository.save(new Examiner(2, "Ewelina", "Cuda", "ewelinka123", "cuda2115", "cuda@pb.edu.pl", true));
        examinerRepository.save(new Examiner(3, "Marta", "Koootara", "kotara", "kotara", "kotara@pb.edu.pl", false));

        examRepository.save(new Exam(1, 6, false, "Projektowanie części w SOLIDWORKS", LocalDate.now(), LocalTime.now().withNano(0), LocalDate.now(), LocalTime.now().plusHours(2).withNano(0), subjectRepository.findById(1).get(), students1));
        examRepository.save(new Exam(2, 0, false, "Retusz zdjęć", LocalDate.now().minusDays(2), LocalTime.now().minusHours(4).withNano(0), LocalDate.now(), LocalTime.now().plusHours(1).withNano(0), subjectRepository.findById(1).get(), students1));
        examRepository.save(new Exam(3, 0, false, "Jak zrobić sprawozdanie w MS Paint", LocalDate.now().minusDays(2), LocalTime.now().plusHours(2).withNano(0), LocalDate.now(), LocalTime.now().plusHours(5).withNano(0), subjectRepository.findById(3).get(), students2));

        examRepository.save(new Exam(4,0,false, "Uzywanie Autocada", LocalDate.now(), LocalTime.now().minusHours(1).withNano(0), LocalDate.now(), LocalTime.now().plusHours(1).withSecond(0).withNano(0).withNano(0), subjectRepository.findById(1).get(), students1));
        examRepository.save(new Exam(5,0,false, "Całki", LocalDate.now().minusDays(2), LocalTime.now().withSecond(0).withNano(0), LocalDate.now(), LocalTime.now().plusHours(1).withSecond(0).withNano(0), subjectRepository.findById(1).get(), students1));
        examRepository.save(new Exam(6,0,false, "Jak zrobić sprawozdanie w notatniku", LocalDate.now().minusDays(2), LocalTime.now().withSecond(0).withNano(0), LocalDate.now(), LocalTime.now().plusHours(1).withSecond(0).withNano(0), subjectRepository.findById(3).get(), students2));


        openQuestionRepository.save(new OpenQuestion(1, "Ile to 5+5?", 10, examRepository.getReferenceById(1)));
        openQuestionRepository.save(new OpenQuestion(2, "Ile to 30*3", 10, examRepository.getReferenceById(1)));
        openQuestionRepository.save(new OpenQuestion(3, "Ile to 19+3?", 10, examRepository.getReferenceById(1)));

        openQuestionRepository.save(new OpenQuestion(4, "Ile to 1+1?", 5, examRepository.getReferenceById(2)));
        openQuestionRepository.save(new OpenQuestion(5, "Ile to 2*2", 5, examRepository.getReferenceById(2)));
        openQuestionRepository.save(new OpenQuestion(6, "Ile to 3+3?", 5, examRepository.getReferenceById(2)));

        closedQuestionRepository.save(new Closedquestion(1, "ile to jest 10*10", 1, examRepository.getReferenceById(1)));
        closedQuestionRepository.save(new Closedquestion(2, "ile to jest 11*11", 1, examRepository.getReferenceById(1)));
        closedQuestionRepository.save(new Closedquestion(3, "ile to jest 12*12", 1, examRepository.getReferenceById(1)));

        closedQuestionRepository.save(new Closedquestion(4, "ile to 4*4", 2, examRepository.getReferenceById(2)));
        closedQuestionRepository.save(new Closedquestion(5, "ile to 5*5", 2, examRepository.getReferenceById(2)));
        closedQuestionRepository.save(new Closedquestion(6, "ile to 6*6", 2, examRepository.getReferenceById(2)));

        answerClosedRepository.save(new Answerclosed(1, "to jest moze 101?", false, closedQuestionRepository.findById(1).get()));
        answerClosedRepository.save(new Answerclosed(2, "to jest moze 102?", false, closedQuestionRepository.findById(1).get()));
        answerClosedRepository.save(new Answerclosed(3, "to jest moze 2003?", false, closedQuestionRepository.findById(1).get()));
        answerClosedRepository.save(new Answerclosed(4, "to jest moze 100?", true, closedQuestionRepository.findById(1).get()));

        answerClosedRepository.save(new Answerclosed(5, "to jest moze 101?", false, closedQuestionRepository.findById(2).get()));
        answerClosedRepository.save(new Answerclosed(6, "to jest moze 121?", true, closedQuestionRepository.findById(2).get()));
        answerClosedRepository.save(new Answerclosed(7, "to jest moze 2003?", false, closedQuestionRepository.findById(2).get()));
        answerClosedRepository.save(new Answerclosed(8, "to jest moze 100?", false, closedQuestionRepository.findById(2).get()));

        answerClosedRepository.save(new Answerclosed(9, "to jest moze 101?", false, closedQuestionRepository.findById(3).get()));
        answerClosedRepository.save(new Answerclosed(10, "to jest moze 102?", false, closedQuestionRepository.findById(3).get()));
        answerClosedRepository.save(new Answerclosed(11, "to jest moze 144?", true, closedQuestionRepository.findById(3).get()));
        answerClosedRepository.save(new Answerclosed(12, "to jest moze 100?", false, closedQuestionRepository.findById(3).get()));

        servicestatisticRepository.save(new Servicestatistic(0, 0, studentsRepository.findAll().size(), examinerRepository.findAll().size()));

        problemRepository.save(new Problem(1, convertImage("/static/images/createExam.jpg"), "Opracowanie metody szybkiego oceniania testów wyboru", student1, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(2, convertImage("/static/images/editExam.jpg"), "Analiza skuteczności egzaminów ustnych online", student1, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(3, convertImage("/static/images/createExam.jpg"), "Badanie wpływu muzyki na wyniki testów", student2, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(4, convertImage("/static/images/editExam.jpg"), "Projektowanie egzaminów z zakresu pierwszej pomocy", student2, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(5, convertImage("/static/images/questionsView.jpg"), "Rozwój kryteriów oceny w szkolnych konkursach artystycznych", student3, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(6, convertImage("/static/images/createExam.jpg"), "Tworzenie testów z architektury zorientowanej na zrównoważony rozwój", student3, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(7, convertImage("/static/images/questionsView.jpg"), "Ocena umiejętności biznesowych na podstawie studiów przypadków", student4, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(8, convertImage("/static/images/createExam.jpg"), "Ewaluacja wpływu mediów na zrozumienie treści nauczania", student4, examiner3, ProblemCategories.BackEnd, examiner3.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(9, convertImage("/static/images/questionsView.jpg"), "Projektowanie testów z zakresu podstaw robotyki", student4, examiner3, ProblemCategories.BackEnd, examiner3.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(10, convertImage("/static/images/editExam.jpg"), "Analiza metod oceny wiedzy o zrównoważonym rolnictwie", student5, examiner3, ProblemCategories.BackEnd, examiner3.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(11, convertImage("/static/images/createExam.jpg"), "Opracowanie sprawiedliwego systemu oceniania egzaminów pisemnych", student5, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(12, convertImage("/static/images/createExam.jpg"), "Projektowanie interaktywnego systemu egzaminowania online", student5, examiner3, ProblemCategories.BackEnd, examiner3.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(13, convertImage("/static/images/questionsView.jpg"), "Badanie wpływu stresu egzaminacyjnego na wyniki studentów", student6, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(14, convertImage("/static/images/editExam.jpg"), "Rozwój metod oceny kreatywności w egzaminach artystycznych", student6, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(15, convertImage("/static/images/questionsView.jpg"), "Analiza efektywności egzaminów ustnych w porównaniu z pisemnymi", student3, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(16, convertImage("/static/images/createExam.jpg"), "Tworzenie systemu zapobiegającego oszustwom na egzaminach online", student2, examiner3, ProblemCategories.BackEnd, examiner3.getFirstname(), ProblemStatus.New));
        problemRepository.save(new Problem(17, convertImage("/static/images/createExam.jpg"), "Badanie różnic w wynikach egzaminów w zależności od pory dnia", student4, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(),ProblemStatus.New));
        problemRepository.save(new Problem(18, convertImage("/static/images/editExam.jpg"), "Optymalizacja formatu egzaminów dla różnych stylów uczenia się", student5, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(),ProblemStatus.New));
        problemRepository.save(new Problem(19, convertImage("/static/images/createExam.jpg"), "Projektowanie egzaminów praktycznych w edukacji medycznej", student1, examiner2, ProblemCategories.BackEnd, examiner2.getFirstname(),ProblemStatus.New));
        problemRepository.save(new Problem(20, convertImage("/static/images/questionsView.jpg"), "Ewaluacja skuteczności egzaminów multimedialnych w nauczaniu języków", student2, examiner1, ProblemCategories.BackEnd, examiner1.getFirstname(),ProblemStatus.New));

        Exam exam1 = examRepository.findById(1).orElse(null);
        Exam exam2 = examRepository.findById(2).orElse(null);
        int maximumScoreExam1 = logstudentexamService.getExamMaximumScore(1);
        int maximumScoreExam2 = logstudentexamService.getExamMaximumScore(2);
        int scoreResult1 = (int)(Math.random() * maximumScoreExam1 + 1);
        int scoreResult2 = (int)(Math.random() * maximumScoreExam1 + 1);
        int scoreResult3 = (int)(Math.random() * maximumScoreExam2 + 1);
        int scoreResult4 = (int)(Math.random() * maximumScoreExam2 + 1);

        logstudentexamRepository.save(new Logstudentexam(1, "Description 1", LocalDate.now(), LocalTime.now(), scoreResult1, exam1,  student1));
        logstudentexamRepository.save(new Logstudentexam(2, "Description 2", LocalDate.now(), LocalTime.now(), scoreResult2, exam1,  student2));
        logstudentexamRepository.save(new Logstudentexam(3, "Description 3", LocalDate.now(), LocalTime.now(), scoreResult3, exam2,  student3));
        logstudentexamRepository.save(new Logstudentexam(4, "Description 4", LocalDate.now(), LocalTime.now(), scoreResult4, exam2,  student4));
    }

    public static byte[] convertImage(String resourcePath) throws Exception {
        ClassPathResource imgFile = new ClassPathResource(resourcePath);
        BufferedImage bImage = ImageIO.read(imgFile.getInputStream());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }
}

