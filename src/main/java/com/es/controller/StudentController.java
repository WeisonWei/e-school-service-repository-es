package com.es.controller;

import com.es.document.Student;
import com.es.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@Api(value = "StudentController", tags = {"Student接口"})
@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@Slf4j
public class StudentController {

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private StudentService studentService;

    @ApiOperation(value = "PostMapping", notes = "PostMapping")
    @PostMapping("/students")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    @ApiOperation(value = "GetMapping", notes = "GetMapping")
    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable Long studentId) {
        return studentService.getStudent(studentId);
    }

    @ApiOperation(value = "PutMapping", notes = "PutMapping")
    @PutMapping("/students")
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @ApiOperation(value = "DeleteMapping", notes = "DeleteMapping")
    @DeleteMapping("/students")
    public Integer deleteStudent(@PathVariable Long studentId) {
        return studentService.deleteStudent(studentId);
    }

}
