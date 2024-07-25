package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam")
public class Exam {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "exam_id", nullable = false)
    private Integer id;

    @Column(name = "question_pool")
    private Integer questionPool;

    @Column(name = "question_pool_strategy")
    private Boolean questionPoolStrategy;

    @Column(name = "description", length = 100)
    private String description;

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", questionPool=" + questionPool +
                ", questionPoolStrategy=" + questionPoolStrategy +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", startTime=" + startTime +
                ", endDate=" + endDate +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", visibility=" + visibility +
                ", examsSubject=" + examsSubject +
                ", conductingExaminer=" + conductingExaminer +
                ", students=" + students +
                '}';
    }

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name="visibility")
    private Boolean visibility;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "exams_subject_id", nullable = false)
    private Subject examsSubject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conducting_examiner_id")
    private Examiner conductingExaminer;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "student_exam",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"),
            foreignKey = @ForeignKey(name ="student_id"),
            inverseForeignKey = @ForeignKey(name = "FK_student_exam_exam")
    )
    private List<Student> students;

    public Exam() {
    }

    public Exam(Integer id, Integer questionPool, Boolean questionPoolStrategy , String description, LocalDate startdate, LocalTime starttime, LocalDate enddate, LocalTime endtime, Subject subject, List<Student> students) {
        this.questionPool = questionPool;
        this.questionPoolStrategy = questionPoolStrategy;
        this.id = id;
        this.description = description;
        this.startDate = startdate;
        this.startTime = starttime;
        this.endDate = enddate;
        this.endTime = endtime;
        this.visibility = false;
        this.examsSubject = subject;
        this.students = students;
        this.duration = Duration.between(LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime)).toMinutes();
    }
}