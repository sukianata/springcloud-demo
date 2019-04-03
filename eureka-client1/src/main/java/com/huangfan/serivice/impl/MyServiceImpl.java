package com.huangfan.serivice.impl;

import com.huangfan.serivice.MyService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/** 
 * @Author:huangfan
 * @Date:下午7:42 2019/4/3
 */
@Service
public class MyServiceImpl implements MyService {

    @HystrixCommand(fallbackMethod = "timeOutMsg")
    @Override
    public String query() {
        try {
            /**
             * 模拟耗时,hystrix的默认超时时间是1s
             */
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "right result from client1";
    }

    /**
     * 注意此处必须是private 且参数列表必须与原方法一直
     * @return
     */
    private String timeOutMsg(){
        return "timeout exception from client1";
    }
}
