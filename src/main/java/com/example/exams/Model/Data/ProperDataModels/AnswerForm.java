package com.example.exams.Model.Data.ProperDataModels;

import com.example.exams.Model.Data.db.Answerclosed;
import com.example.exams.Model.Data.db.Closedquestion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class AnswerForm {
    private Closedquestion question;
    private List<Answerclosed> answers;

    public void printAnswersContent() {
        if (answers != null) {
            for (Answerclosed answer : answers) {
                System.out.println(answer.getDescription());
                System.out.println(answer.getId());
                System.out.println(answer.getClosedquestionQuestionid());
                System.out.println(answer.isCorrect());
            }
        }
    }
}
