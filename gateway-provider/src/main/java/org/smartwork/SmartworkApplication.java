package org.smartwork;

import org.forbes.comm.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages={"org.forbes","org.smartwork"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.forbes.comm.service"})
@EnableZuulProxy
public class SmartworkApplication {

    public static void main(String[] args) {
        System.setProperty(CommonConstant.PROVILES_CODE,"prod");
        SpringApplication.run(SmartworkApplication.class, args);
    }
}
