package com.example.springbootdemo.springbean;


import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class Person {

    @Autowired
    private Animal animal;




    @PostConstruct
    public void testPostConstruct() {
        System.out.println("\"testPostConstruct\" = " + "testPostConstruct");
    }
}
