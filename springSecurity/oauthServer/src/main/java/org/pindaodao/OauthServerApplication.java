package org.pindaodao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableDiscoveryClient
@MapperScan(value = "org.pindaodao.**.mapper")
public class OauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthServerApplication.class, args);
    }
}
