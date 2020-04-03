package com.es.repository;

import com.es.document.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEsRepository extends ElasticsearchRepository<Student, Long> {
}
