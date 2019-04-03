package com.huangfan.feign;

import org.springframework.stereotype.Component;
/**
 * @Author:huangfan
 * @Date:下午4:00 2019/4/3
 * feign服务降级类
 */
@Component
public class FeignCallBackServiceImpl implements FeignService {
    @Override
    public String queryByFeign() {
        return "feign读取超时，服务降级后返回";
    }
}
