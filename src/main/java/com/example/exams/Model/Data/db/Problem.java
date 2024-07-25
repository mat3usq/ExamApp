package com.example.exams.Model.Data.db;

import com.example.exams.Model.Data.ProperDataModels.ProblemCategories;
import com.example.exams.Model.Data.ProperDataModels.ProblemStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "problem")
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "problemid", nullable = false)
    private Integer id;

    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(name = "description", length = 100)
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "problems_student_id")
    private Student problemsStudent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "problems_examiner_id")
    private Examiner problemsExaminer;

    @Column(name = "category")
    private ProblemCategories category;

    @Column(name = "username", length = 20)
    private String username;

    @Column(name = "status")
    private ProblemStatus status = ProblemStatus.New;
}