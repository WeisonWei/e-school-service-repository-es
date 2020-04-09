package com.es.service;

import com.es.document.Student;
import com.es.repository.StudentEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StudentEsRepository studentEsRepository;

    @Value("${spring.date.elasticsearch.cluster-node}")
    String esUrl;


    @Override
    public List<Student> getStudents() {
        Iterable<Student> all = studentEsRepository.findAll();
        return (List) all;
    }

    @Override
    public Student getStudent(Long studentId) {
        return null;
    }

    @Override
    public Student updateStudent(Student student) {
        return null;
    }

    @Override
    public Integer deleteStudent(Long studentId) {
        return null;
    }

    @Override
    public String getStudentByIdFromRest(Long studentId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", studentId);
        String result = restTemplate.getForObject(esUrl, String.class, params);
        return null;
    }

    @Override
    public String getStudentByIdFromJpa(Long studentId) {
        return null;
    }
}
