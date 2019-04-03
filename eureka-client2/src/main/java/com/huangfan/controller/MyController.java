package com.huangfan.controller;

import com.huangfan.serivice.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:huangfan
 * @Date:下午7:44 2019/4/3
 */
@RestController
public class MyController {

    @Autowired
    private MyService service;

    @RequestMapping(value = "/query.ajax")
    public String query() {
        return service.query();
    }
}
