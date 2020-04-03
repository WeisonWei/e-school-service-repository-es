package com.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EntityScan(basePackages = {"com.es.repository.document"})
//@EnableElasticsearchRepositories(basePackages = "com.es.repository.repository", queryLookupStrategy = CREATE_IF_NOT_FOUND)
@EnableElasticsearchRepositories(basePackages = {"com.es.repository.repository"})
public class ESchoolServiceRepositoryEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESchoolServiceRepositoryEsApplication.class, args);
    }

}
