package com.example.exams.Model.Data.ProperDataModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login implements Serializable {

    private String username;

    private String password;
}
