package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {

}