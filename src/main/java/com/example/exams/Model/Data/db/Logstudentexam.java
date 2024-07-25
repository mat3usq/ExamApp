package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logstudentexam")
public class Logstudentexam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logstudentexamid", nullable = false)
    private Integer id;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Time")
    private LocalTime time;

    @Column(name = "score_result")
    private Integer scoreresult;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "exam_examid")
    private Exam examExamid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_studentid")
    private Student studentStudent;

    public void addPoints(int points) {
        if (this.scoreresult == null) {
            this.scoreresult = 0;
        }
        this.scoreresult += points;
    }





}