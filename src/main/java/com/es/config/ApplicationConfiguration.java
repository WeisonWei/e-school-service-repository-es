/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.es.config;

import com.es.document.Student;
import com.es.repository.StudentEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class ApplicationConfiguration {

    @Qualifier("elasticsearchTemplate")
    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    StudentEsRepository repository;

    @PostConstruct
    public void insertDataSample() {
        operations.refresh(Student.class);

        // Save data sample
        repository.save(Student.builder().id(10l).age(1).name("Spring eXchange 2014 - London").build());
        repository.save(Student.builder().id(11l).age(2).name("Scala eXchange 2014 - London").build());
        repository.save(Student.builder().id(12l).age(3).name("Elasticsearch 2014 - Berlin").build());
        repository.save(Student.builder().id(12l).age(4).name("AWS London 2014").build());
        repository.save(Student.builder().id(13l).age(5).name("JDD14 - Cracow").build());
    }

    @PreDestroy
    public void deleteIndex() {
        operations.deleteIndex(Student.class);
    }
}
