package com.example.exams.Model.Data.ProperDataModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenQuestionDTO {
    private Integer questionid;

    @JsonProperty("content")
    private String content;

    @JsonProperty("score")
    private Integer score;
    private Integer subjectId;
    private Integer examId;
}

