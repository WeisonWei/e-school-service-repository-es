package com.es.modle;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private String name;

    private int age;

    private Double money;

    private String address;

    private String birthday;
}
