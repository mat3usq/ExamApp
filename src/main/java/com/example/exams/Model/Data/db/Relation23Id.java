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
public class Relation23Id implements Serializable {
    private static final long serialVersionUID = -3265220597071189018L;
    @Column(name = "group_classid", nullable = false)
    private Integer groupClassid;

    @Column(name = "egzaminator_egzaminator_id", nullable = false)
    private Integer egzaminatorEgzaminatorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Relation23Id entity = (Relation23Id) o;
        return Objects.equals(this.egzaminatorEgzaminatorId, entity.egzaminatorEgzaminatorId) &&
                Objects.equals(this.groupClassid, entity.groupClassid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(egzaminatorEgzaminatorId, groupClassid);
    }

}