package org.pindaodao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CilentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CilentApplication.class, args);
    }
}
