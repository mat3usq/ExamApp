package com.example.exams.Repositories.Db;

import com.example.exams.Model.Data.db.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GroupEntityRepository extends JpaRepository<Group, Integer> {
    Group findGroupById(Integer groupId);
}
