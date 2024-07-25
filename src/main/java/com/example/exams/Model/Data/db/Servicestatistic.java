package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "servicestatistics")
public class Servicestatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statisticsID", nullable = false)
    private Integer id;

    @Column(name = "visitorscount")
    private Integer visitorscount;

    @Column(name = "examscount")
    private Integer examscount;

    @Column(name = "studentscount")
    private Integer studentscount;

    @Column(name = "examinatorscount")
    private Integer examinatorscount;

    @Column(name = "startdate")
    private LocalDate startdate;

    public Servicestatistic() {
    }
    public Servicestatistic(Integer visitorscount, Integer examscount, Integer studentscount,Integer examinatorscount){
        this.visitorscount = visitorscount;
        this.examscount =  examscount;
        this.studentscount = studentscount;
        this.examinatorscount = examinatorscount;
        this.startdate = LocalDate.now();
    }

}