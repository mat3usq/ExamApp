package com.example.exams.Services;

import com.example.exams.Model.Data.ProperDataModels.Login;
import com.example.exams.Repositories.LoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginsService {
    @Autowired
    LoginsRepository loginsRepository;

    public List<Login> getAllLogins() {
        return loginsRepository.getLoginList();
    }
}
