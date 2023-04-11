package com.pindaodao.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.pindaodao.autoconfigure.pojo.User;

@Configuration
@EnableConfigurationProperties(PindaodaoProperties.class)
public class PindaodaoAutoconfigure {

    @Autowired
    private PindaodaoProperties pindaodaoProperties;

    @Bean
    public User user() {
        return new User(pindaodaoProperties.getName(),
                pindaodaoProperties.getPassword(),
                pindaodaoProperties.getUnit(),
                pindaodaoProperties.getIdCard());
    }
}
