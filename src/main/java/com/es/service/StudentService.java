package com.es.service;

public interface StudentService {

    String getStudentByIdFromRest(Long studentId);

    String getStudentByIdFromJpa(Long studentId);
}
