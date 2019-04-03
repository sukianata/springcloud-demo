package com.huangfan.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * eureka-client指服务名
 * fallback指定服务降级时的类
 */
/** 
 * @Author:huangfan
 * @Date:下午7:45 2019/4/3
 */
@FeignClient(value="eureka-client",fallback = FeignCallBackServiceImpl.class)
public interface FeignService {

    /**
     * query.ajax指服务中的某个方法
     * @return
     */
    @RequestMapping("/query.ajax")
    public String queryByFeign();
}
