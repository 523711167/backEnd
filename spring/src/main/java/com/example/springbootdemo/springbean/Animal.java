package com.example.springbootdemo.springbean;


import org.springframework.beans.factory.annotation.Autowired;

public class Animal {

    @Autowired
    private Person person;
}
