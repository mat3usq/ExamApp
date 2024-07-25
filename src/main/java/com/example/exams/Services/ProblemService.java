package com.example.exams.Services;

//import com.example.exams.Model.Data.ProperDataModels.ProblemDTO;

import com.example.exams.Model.Data.ProperDataModels.ProblemCategories;
import com.example.exams.Model.Data.ProperDataModels.ProblemStatus;
import com.example.exams.Model.Data.ProperDataModels.ShowProblem;
import com.example.exams.Model.Data.db.Problem;
import com.example.exams.Repositories.Db.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProblemService {
    ProblemRepository problemRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public Problem AddOne(Problem problem) {
        return problemRepository.save(problem);
    }

    public List<Problem> GetAll() {
        return problemRepository.findAll();
    }

    public Problem findById(int id) {
        return problemRepository.getReferenceById(id);
    }

    public Problem changeStatus(int id, ProblemStatus problemStatus) {
        Problem problem = findById(id);
        problem.setStatus(problemStatus);
        return problemRepository.save(problem);
    }

    public Map<String, List<ShowProblem>> getProblemsBySelectedCategory(Map<String, List<ShowProblem>> problemsByCategory, String selectedCategory) {
        Map<String, List<ShowProblem>> filteredProblemsByCategory = new HashMap<>();
        if (selectedCategory == null || selectedCategory.equals("")) {
            return new HashMap<>(problemsByCategory);
        } else {
            filteredProblemsByCategory.put(selectedCategory, problemsByCategory.getOrDefault(selectedCategory, Collections.emptyList()));
        }

        return filteredProblemsByCategory;
    }
    public static Map<String, List<ShowProblem>> getProblemsBySelectedStatus(Map<String, List<ShowProblem>> problemsByStatus, String selectedStatus) {
        Map<String, List<ShowProblem>> filteredProblemsByCategory = new HashMap<>();
        if (selectedStatus == null || selectedStatus.isEmpty() || selectedStatus.equals("")) {
            return problemsByStatus;
        }

        for (Map.Entry<String, List<ShowProblem>> entry : problemsByStatus.entrySet()) {
            List<ShowProblem> problems = entry.getValue();
            List<ShowProblem> filteredProblems = new ArrayList<>();

            for (ShowProblem problem : problems) {
                if (problem.getStatus().equals(selectedStatus)) {
                    filteredProblems.add(problem);
                }
            }

            if (!filteredProblems.isEmpty()) {
                filteredProblemsByCategory.put(entry.getKey(), filteredProblems);
            }
        }

        return filteredProblemsByCategory;
    }
    public Map<String, List<ShowProblem>> getProblemsByCategoryAndStatus(Map<String, List<ShowProblem>> problemsByCategory, String selectedCategory, String selectedStatus) {
        Map<String, List<ShowProblem>> filteredProblemsByCategory = new HashMap<>();

        if (selectedCategory == null || selectedCategory.isEmpty()) {
            return getProblemsBySelectedStatus(problemsByCategory,selectedStatus);
        }

        if (selectedStatus == null || selectedStatus.isEmpty()) {
            return getProblemsBySelectedCategory(problemsByCategory, selectedCategory);
        }

        for (Map.Entry<String, List<ShowProblem>> entry : problemsByCategory.entrySet()) {
            String category = entry.getKey();
            List<ShowProblem> categoryProblems = entry.getValue();
            List<ShowProblem> filteredProblems = new ArrayList<>();

            for (ShowProblem problem : categoryProblems) {
                if (problem.getCategory().equals(selectedCategory) && problem.getStatus().equals(selectedStatus)) {
                    filteredProblems.add(problem);
                }
            }

            if (!filteredProblems.isEmpty()) {
                filteredProblemsByCategory.put(category, filteredProblems);
            }
        }

        return filteredProblemsByCategory;
    }

    public List<String> getUniqueCategories() {
        return  Arrays.stream(ProblemCategories.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
    public List<String> getUniqueStatuses() {
        return Arrays.stream(ProblemStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
