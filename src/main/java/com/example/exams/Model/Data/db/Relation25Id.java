package com.example.exams.Model.Data.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class Relation25Id implements Serializable {
    private static final long serialVersionUID = 5197636945612056163L;
    @Column(name = "subject_subjectid", nullable = false)
    private Integer subjectSubjectid;

    @Column(name = "group_classid", nullable = false)
    private Integer groupClassid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Relation25Id entity = (Relation25Id) o;
        return Objects.equals(this.groupClassid, entity.groupClassid) &&
                Objects.equals(this.subjectSubjectid, entity.subjectSubjectid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupClassid, subjectSubjectid);
    }

}