package com.huangfan.service.impl;

import com.huangfan.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonServiceImpl implements RibbonService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String query() {
        return restTemplate.getForObject("http://EUREKA-CLIENT/query.ajax", String.class);
    }
}
