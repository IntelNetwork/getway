package org.smartwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages={"org.forbes","org.smartwork"},exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.forbes.comm.service"})
public class SmartworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartworkApplication.class, args);
    }
}
