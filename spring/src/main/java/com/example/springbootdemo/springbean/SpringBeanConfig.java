package com.example.springbootdemo.springbean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {Animal.class, Person.class})
public class SpringBeanConfig {
}
