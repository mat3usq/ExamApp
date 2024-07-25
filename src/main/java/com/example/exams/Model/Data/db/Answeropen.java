package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answeropen")
public class Answeropen {
    @Id
    @Column(name = "answerid", nullable = false)
    private Integer id;

    @Column(name = "description", length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "openquestion_questionid", nullable = false)
    private OpenQuestion openquestionQuestionid;


    @Override
    public String toString() {
        return "Answeropen{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", openquestionQuestionid=" + openquestionQuestionid +
                '}';
    }
}