package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subjectid", nullable = false)
    private Integer id;

    @Column(name = "name", length = 20)
    private String name;

    public Subject() {}

    public Subject(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}