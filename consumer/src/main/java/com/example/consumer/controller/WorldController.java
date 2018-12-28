package com.example.consumer.controller;

import com.example.consumer.service.WorldService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @Author NicholasLiu
 * @Date 2018/12/28 15:27
 **/
@RestController
public class WorldController {

    public static final String BASEURI = "http://PROVIDER";

    @Autowired
    private WorldService worldService;


    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "processHystrix_Get"
            ,commandProperties  = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")})
    @RequestMapping("/haha")
    public String send() {
        return restTemplate.getForEntity(BASEURI + "/aa",String.class).getBody();
    }

    public String processHystrix_Get() {
        return "服务器开小差了.......";
    }


    @RequestMapping("/bb")
    public String getBB() {
        return worldService.getAA();
    }

}
