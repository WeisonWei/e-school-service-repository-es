package com.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ESchoolServiceRepositoryEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESchoolServiceRepositoryEsApplication.class, args);
    }

}
