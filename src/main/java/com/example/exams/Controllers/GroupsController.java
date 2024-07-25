package com.example.exams.Controllers;

import com.example.exams.Model.Data.db.Group;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Services.GroupsService;
import com.example.exams.Services.StudentsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;


@Controller
public class GroupsController {
    final StudentsService studentsService;
    final GroupsService groupsService;

    public GroupsController(StudentsService studentsService, GroupsService groupsService){
        this.studentsService = studentsService;
        this.groupsService = groupsService;
    }

    @GetMapping("/groups")
    public ModelAndView groups(){
        ModelAndView modelAndView = new ModelAndView();
        List<Group> groups = groupsService.getAllGroups();
        modelAndView.addObject("groups", groups);
        modelAndView.setViewName("groupsView");
        modelAndView.addObject("groups", groups);
        return modelAndView;
    }

    @GetMapping("/addGroup")
    public ModelAndView addGroups(){
        ModelAndView modelAndView = new ModelAndView();
        Group group = new Group();
        List<Student> students = studentsService.getAllStudents();
        modelAndView.setViewName("addGroup");
        modelAndView.addObject("group", group);
        modelAndView.addObject("students", students);
        return modelAndView;
    }

    @PostMapping("/addGroup")
    public String addGroup(@ModelAttribute("group") Group group){
        System.out.println("Adding students to group: " + group.getStudents());
        groupsService.addGroup(group);
        return "redirect:/groups";
    }
    @GetMapping("/deleteGroup/{groupId}")
    public String deleteGroup(@PathVariable Integer groupId){
        groupsService.deleteGroup(groupId);
        return "redirect:/groups";
    }

    @GetMapping("/manageGroup/{groupId}")
    public ModelAndView manageGroup(@PathVariable Integer groupId){
        ModelAndView modelAndView = new ModelAndView();

        Group group = this.groupsService.getGroupByGroupId(groupId);

        List<Student> addedStudents = group.getStudents();
        List<Student> allStudents = studentsService.getAllStudents();

        List<Student> availableStudents = allStudents.stream()
                .filter(student -> !addedStudents.contains(student))
                .collect(Collectors.toList());

        modelAndView.addObject("students", availableStudents);
        modelAndView.addObject("addedStudents", addedStudents);

        modelAndView.setViewName("manageGroup");
        return modelAndView;
    }

    @GetMapping("/addStudentToGroup/{groupId}")
    public ModelAndView addStudent(@PathVariable Integer groupId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addStudentToGroup");
        modelAndView.addObject("groupId", groupId);

        List<Student> students = studentsService.getAllStudents();
        modelAndView.addObject("students", students);

        return modelAndView;
    }

    @GetMapping("/addStudentsToGroup")
    public String addStudentsToGroup(@RequestParam("groupId") int groupId, @RequestParam("StudentsToAdd") List<Integer> studentIds) {
        groupsService.addStudentsToGroup(groupId,studentIds);
        return "redirect:/manageGroup/" + groupId;
    }


    @PostMapping("/addStudent")
    public String addStudent(@RequestParam("groupId") String groupId, @RequestParam("studentId") Integer studentId) {
        System.out.println(groupId);
        System.out.println(studentId);
        try{
            int parsedGroupId = Integer.parseInt(groupId);
            this.groupsService.addStudent(parsedGroupId, studentId);
        }catch(NumberFormatException e){
            System.err.println("Parsing error - groupId: " + groupId);
        }
        return "redirect:/manageGroup/" + groupId;
    }
    @PostMapping("/deleteStudent")
    public String deleteStudent(@RequestParam("groupId") String groupId, @RequestParam("studentId") String studentId){
        try {
            int parsedGroupId = Integer.parseInt(groupId);
            int parsedStudentId = Integer.parseInt(studentId);
            this.groupsService.removeStudent(parsedGroupId, parsedStudentId);
        }catch (NumberFormatException e) {
            System.err.println("Parsing error - groupId: " + groupId);
        }
        return "redirect:/manageGroup/" + groupId;
    }

    @GetMapping("/editStudent/{groupId}/{studentId}")
    public ModelAndView showEditStudentForm(@PathVariable Integer groupId, @PathVariable Integer studentId){
        ModelAndView modelAndView = new ModelAndView();

        Student student = this.studentsService.getStudentById(studentId);

        modelAndView.addObject("student", student);
        modelAndView.addObject("groupId", groupId);

        modelAndView.setViewName("editStudent");
        return modelAndView;
    }
    @PostMapping("/editStudent")
    public String editStudent(@ModelAttribute("editedStudent") Student editedStudent, @ModelAttribute("groupId") String groupId){
        Integer studentId = editedStudent.getStudentId();
        String firstName = editedStudent.getFirstname();
        String lastName = editedStudent.getLastname();
        String login = editedStudent.getLogin();
        String password = editedStudent.getPassword();
        String email = editedStudent.getEmail();

        studentsService.editStudent(studentId, firstName, lastName, login, password, email);
        return "redirect:/manageGroup/" + groupId;
    }
}
