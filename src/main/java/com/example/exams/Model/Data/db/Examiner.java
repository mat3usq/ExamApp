package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "examiner")
public class Examiner {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "examiner_id", nullable = false)
    private Integer examiner_id;

    @Column(name = "firstname", nullable = false, length = 20)
    private String firstname;

    @Column(name = "lastname", length = 20)
    private String lastname;

    @Column(name = "login", length = 20)
    private String login;

    @Column(name = "password", length = 20)
    private String password;

    @Column(name = "email", length = 40)
    private String email;

    @Column(name = "verification_status", nullable = false)
    private boolean verificationStatus;

    @OneToMany(mappedBy = "problemsExaminer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Problem> problems;

    @OneToMany(mappedBy = "conductingExaminer",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Exam> exams;

    @OneToMany(mappedBy = "egzaminatorEgzaminator",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Relation23> relation23s;

    public Examiner() {}

    public Examiner(Integer examiner_id, String firstname, String lastname, String login, String password, String email, boolean verificationStatus) {
        this.examiner_id = examiner_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.verificationStatus = verificationStatus;
    }
}