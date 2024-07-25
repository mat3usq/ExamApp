package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Administrator;
import com.example.exams.Model.Data.db.Examiner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdministratorsEntityRepository extends JpaRepository<Administrator, Integer> {
    Administrator findAdministratorByLogin(String login);
    Administrator findAdministratorByEmail(String email);
    @Query("SELECT a FROM Administrator a WHERE a.administrator_id = :id")
    Administrator findAdministratorById(@Param("id") Integer id);



}