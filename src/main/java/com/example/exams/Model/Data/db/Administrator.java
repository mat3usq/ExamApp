package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrator_id", nullable = false)
    private Integer administrator_id;

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

    public Administrator() {}

    public Administrator(Integer administrator_id, String firstname, String lastname, String login, String password, String email, boolean verificationStatus) {
        this.administrator_id = administrator_id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.verificationStatus = verificationStatus;
    }
}