package com.example.exams.Model.Data.ProperDataModels;

import com.example.exams.Model.Data.db.Examiner;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowProblem {
    private int id;
    private String description;
    private String photo;
    private String category;
    private String username;
    private String status;
    private Examiner examiner;
}
