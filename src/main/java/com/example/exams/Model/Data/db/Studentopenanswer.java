package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "studentopenanswer")
public class Studentopenanswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid", nullable = false)
    private Integer id;

    @Column(name = "score")
    private Integer score;


    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "Time", nullable = false)
    private LocalTime time;

    @Column(name = "description", length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "openquestion_questionid")
    private OpenQuestion openquestionQuestionid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_student_id")
    private Student studentStudent;

    @Override
    public String toString() {
        return "Studentopenanswer{" +
                "id=" + id +
                ", score=" + score +
                ", date=" + date +
                ", time=" + time +
                ", description='" + description + '\'' +
                ", openquestionQuestionid=" + openquestionQuestionid +
                ", studentStudent=" + studentStudent +
                '}';
    }
}