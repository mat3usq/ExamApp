package com.example.exams.Services;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import com.example.exams.Model.Data.ProperDataModels.User;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Repositories.Db.AdministratorsEntityRepository;
import com.example.exams.Repositories.Db.ExaminerRepository;
import com.example.exams.Repositories.Db.StudentsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    AdministratorsEntityRepository administratorsEntityRepository;

    @Autowired
    ExaminerRepository examinersRepository;

    @Autowired
    StudentsEntityRepository studentsEntityRepository;

    public void addAdministratorToDB(Administrator administrator){
        administratorsEntityRepository.save(administrator);
    }

    public void addAExaminerToDB(Examiner examiner){
        examinersRepository.save(examiner);
    }

    public void addAStudentToDB(Student student) {
        studentsEntityRepository.save(student);
    }

    public Administrator mapToAdministratorEntity(User user) {
        Administrator administrator = new Administrator();
        administrator.setFirstname(user.getFirstname());
        administrator.setLastname(user.getLastname());
        administrator.setLogin(user.getLogin());
        administrator.setPassword(user.getPassword());
        administrator.setEmail(user.getEmail());
        administrator.setVerificationStatus(false);
        return administrator;
    }

    public Examiner mapToExaminerEntity(User user) {
        Examiner examiner = new Examiner();
        examiner.setFirstname(user.getFirstname());
        examiner.setLastname(user.getLastname());
        examiner.setLogin(user.getLogin());
        examiner.setPassword(user.getPassword());
        examiner.setEmail(user.getEmail());
        examiner.setVerificationStatus(false);
        return examiner;
    }

    public Student mapToStudentEntity(User user) {
        Student student = new Student();
        student.setFirstname(user.getFirstname());
        student.setLastname(user.getLastname());
        student.setLogin(user.getLogin());
        student.setPassword(user.getPassword());
        student.setEmail(user.getEmail());
        return student;
    }

    public Administrator getAdministratorByLogin(String login){
        return administratorsEntityRepository.findAdministratorByLogin(login);
    }

    public Examiner getExaminerByLoginAndPassword(String login){
        return examinersRepository.findByLogin(login);
    }

    public Student getStudentByid(int id){
        return studentsEntityRepository.findById(id).orElse(null);
    }
    public Student getStudentByEmail(String studentEmail) {
        List<Student> students = studentsEntityRepository.findAll();
        for (Student student : students) {
            if (student.getEmail().equals(studentEmail)) {
                return student;
            }
        }
        return null;
    }

    public String determinePermissions(String email) {
        if (email.endsWith("@student.pb.edu.pl")) {
            return "Student";
        } else if (email.endsWith("@examiner.pb.edu.pl")) {
            return "Examiner";
        } else if (email.endsWith("@administrator.pb.edu.pl")) {
            return "Administrator";
        } else {
            return "UnknownTypeOfUser";
        }
    }

}
