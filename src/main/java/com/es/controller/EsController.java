package com.es.controller;

import com.es.document.Student;
import com.es.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EsController {

    @Autowired
    StudentService studentService;

    @PostMapping("/students")
    public List<Student> getStudents() {
        List<Student> students = studentService.getStudents();
        return students;
    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable Long studentId) {
        Student student = studentService.getStudent(studentId);
        return student;
    }

    @PutMapping("/students")
    public Student updateStudent(@RequestBody Student student) {
        Student student1 = studentService.updateStudent(student);
        return student1;
    }

    @DeleteMapping("/students")
    public Integer deleteStudent(@PathVariable Long studentId) {
        Integer num = studentService.deleteStudent(studentId);
        return num;
    }

}
