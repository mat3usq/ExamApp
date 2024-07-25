package com.example.exams.Model.Data.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "relation_25")
public class Relation25 {
    @EmbeddedId
    private Relation25Id id;

    @MapsId("subjectSubjectid")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "subject_subjectid", nullable = false)
    private Subject subjectSubjectid;

    @MapsId("groupClassid")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "group_classid", nullable = false)
    private Group groupClassid;

}