package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Servicestatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicestatisticRepository extends JpaRepository<Servicestatistic, Integer> {
    public Servicestatistic getServicestatisticById(int id);

}