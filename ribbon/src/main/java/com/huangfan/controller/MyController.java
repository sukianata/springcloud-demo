package com.huangfan.controller;

import com.huangfan.feign.FeignService;
import com.huangfan.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 
 * @Author:huangfan
 * @Date:下午7:45 2019/4/3
 */
@RestController
public class MyController {
    @Autowired
    private RibbonService service;

    @Autowired
    private FeignService feignService;
    @RequestMapping("/query")
    public String query(){
        return service.query();
    }

    @RequestMapping("/query2")
    public String queryByFeign(){
        System.out.println("query by feign");
        return feignService.queryByFeign();
    }
}
