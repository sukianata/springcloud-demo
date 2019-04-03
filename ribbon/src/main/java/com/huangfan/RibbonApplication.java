package com.huangfan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/** 
 * @Author:huangfan
 * @Date:下午7:45 2019/4/3
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients  /** 支持Feign **/
public class RibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(RibbonApplication.class);
    }

    /**
     * @LoadBalanced 注意该注解是Ribbon原始的负载均衡策略，当采用feign时，feign会对此进行封装，故需要移出
     * @return
     */
    @Bean
//    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
