package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logsid", nullable = false)
    private Integer id;

    @Column(name = "description", length = 400)
    private String description;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "Time", columnDefinition = "TIME")
    private LocalTime time;

    public Log() {
    }
    public Log(String description) {
        this.description = description;
        this.date = LocalDate.now();
        this.time = LocalTime.now().withNano(0);
    }

}