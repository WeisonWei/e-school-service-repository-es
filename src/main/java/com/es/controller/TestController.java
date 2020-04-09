package com.es.controller;

import com.es.document.Student;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private ElasticsearchOperations elasticsearchOperations;

    public TestController(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @PostMapping("/person")
    public String save(@RequestBody Student student) {

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(student.getId().toString())
                .withObject(student)
                .build();
        String documentId = elasticsearchOperations.index(indexQuery);
        return documentId;
    }

    @GetMapping("/person/{id}")
    public Student findById(@PathVariable("id") Long id) {
        Student student = elasticsearchOperations
                .queryForObject(GetQuery.getById(id.toString()), Student.class);
        return student;
    }
}

