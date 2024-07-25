package com.example.exams.Model.Data.ProperDataModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ExamDTO {
    @JsonProperty("subject_id")
    private Integer subjectId;

    @JsonProperty("description")
    private String description;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("questions")
    private List<OpenQuestionDTO> questions;

    @JsonProperty("egzaminator")
    private Integer egzamiantor;
}
