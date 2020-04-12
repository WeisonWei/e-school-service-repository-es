package com.es.service;

import com.es.document.Student;

import java.util.List;

public interface StudentService {

    List<Student> getStudents();

    Student getStudent(Long studentId);

    Student updateStudent(Student student);

    Integer deleteStudent(Long studentId);

    String getStudentByIdFromRest(Long studentId);

    String getStudentByIdFromJpa(Long studentId);
}
