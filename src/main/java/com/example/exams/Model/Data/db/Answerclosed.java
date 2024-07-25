package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "answerclosed")
public class Answerclosed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerid", nullable = false)
    private Integer id;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "isCorrect")
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "closedquestion_questionid", nullable = false)
    private Closedquestion closedquestionQuestionid;

    public Answerclosed(Integer answerId, String description, boolean correct, Closedquestion question) {
        this.id = answerId;
        this.description = description;
        this.isCorrect = correct;
        this.closedquestionQuestionid = question;
    }

    @Override
    public String toString() {
        return "Answerclosed{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", isCorrect=" + isCorrect +
                ", closedquestionQuestionid=" + closedquestionQuestionid +
                '}';
    }
}