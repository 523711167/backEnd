package com.pindaodoa.configuration;

import com.pindaodoa.pojo.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfiguration {

    @Autowired
    private com.pindaodao.autoconfigure.pojo.User user;

    @Bean
    public A a() {
        System.out.println(user);
        return new A();
    }
}
