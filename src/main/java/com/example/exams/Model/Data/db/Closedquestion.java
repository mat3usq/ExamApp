package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "closedquestion")
public class Closedquestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closed_question_id", nullable = false)
    private Integer id;



    @Column(name = "content", length = 100)
    private String content;

    @Column(name = "score")
    private Integer score;



    @ManyToOne(fetch = FetchType.EAGER,optional = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "exam_id", nullable = true)
    private Exam exam;

    public Closedquestion(Integer closed_question_id, String content, Integer score, Exam exam) {
        this.id = closed_question_id;
        this.content = content;
        this.score = score;
        this.exam = exam;
    }

    @Override
    public String toString() {
        return "Closedquestion{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", exam=" + exam +
                '}';
    }
}