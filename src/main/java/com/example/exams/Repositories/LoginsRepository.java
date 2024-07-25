package com.example.exams.Repositories;

import com.example.exams.Model.Data.ProperDataModels.Login;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Data
public class LoginsRepository {
    List<Login> loginList = new ArrayList<>();
}
