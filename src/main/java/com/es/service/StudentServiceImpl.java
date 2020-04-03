package com.es.service;

import com.es.repository.StudentEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    /*@Autowired
    RestTemplate restTemplate;*/

    @Autowired
    StudentEsRepository studentEsRepository;

    @Value("${spring.date.elasticsearch.cluster-node}")
    String esUrl;


    @Override
    public String getStudentByIdFromRest(Long studentId) {
        //HashMap<String, Object> params = new HashMap<>();
        //params.put("id", studentId);
        //String result = restTemplate.getForObject(esUrl, String.class, params);
        return null;
    }

    @Override
    public String getStudentByIdFromJpa(Long studentId) {
        return null;
    }
}
