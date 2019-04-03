package com.huangfan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/** 
 * @Author:huangfan
 * @Date:下午7:42 2019/4/3
 */
@SpringBootApplication
@EnableEurekaClient //服务启动后会自动注册到Eureka服务中
@EnableCircuitBreaker //支持hystrix的熔断机制
public class EurekaClient1Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClient1Application.class);
    }
}
