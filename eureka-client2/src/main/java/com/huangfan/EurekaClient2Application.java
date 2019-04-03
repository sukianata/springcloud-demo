package com.huangfan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient   /**启动服务后会想Eureka注册服务**/
@EnableCircuitBreaker /**支持Hystrix**/

/**
 * @author:huangfan
 * @Date:下午2:09 2019/4/3
 */
public class EurekaClient2Application {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClient2Application.class);
    }
}
