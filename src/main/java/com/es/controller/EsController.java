package com.es.controller;

import com.es.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsController {

    @Autowired
    StudentService studentService;

    @PostMapping("/student/{studentId}")
    public String getStudentById(@PathVariable Long studentId) {
        String student = studentService.getStudentByIdFromRest(studentId);
        return student;
    }

}
