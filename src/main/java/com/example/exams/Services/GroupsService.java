package com.example.exams.Services;

import com.example.exams.Model.Data.db.Group;
import com.example.exams.Model.Data.db.Student;
import com.example.exams.Repositories.Db.GroupEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupsService {
    @Autowired
    GroupEntityRepository groupsRepository;

    @Autowired
    StudentsService studentsService;

    public List<Group> getAllGroups() {
        return groupsRepository.findAll();
    }

    public Group getGroupByGroupId(Integer groupId) {
        return groupsRepository.findGroupById(groupId);
    }

    public void removeStudent(Integer groupId, Integer studentId) {
        Group group = groupsRepository.findGroupById(groupId);
        Student student = studentsService.getStudentById(studentId);

        if(group != null && student != null) {
            if(group.getStudents().contains(student)){
                group.getStudents().remove(student);
                groupsRepository.save(group);
            }
        }
    }
    public void addStudentsToGroup(Integer groupId, List<Integer> studentIds) {
        Group group = groupsRepository.findGroupById(groupId);

        if (group != null) {
            List<Student> students = studentsService.getStudentsByIds(studentIds);

            if (!students.isEmpty()) {
                for (Student student : students) {
                    if (!group.getStudents().contains(student)) {
                        group.getStudents().add(student);
                    }
                }

                groupsRepository.save(group);
            }
        }
    }


    public void addStudent(Integer groupId, Integer studentId) {
        Group group = groupsRepository.findGroupById(groupId);
        Student student = studentsService.getStudentById(studentId);

        if(group != null && student != null) {
            if(!group.getStudents().contains(student)){
                group.getStudents().add(student);
                groupsRepository.save(group);
            }
        }
    }
    public boolean deleteGroup(Integer groupId) {
        Group group = getGroupByGroupId(groupId);
        List<Student> addedStudents = group.getStudents();
        for(int i=0;i<addedStudents.size();i++){
            removeStudent(groupId, addedStudents.get(i).getStudentId());
        }
        if (groupsRepository.existsById(groupId)) {
            groupsRepository.deleteById(groupId);
            return true;
        }
        return false;
    }

    public void addGroup(Group group) {
        groupsRepository.save(group);
    }
}
