package com.example.consumer.service;

import com.example.consumer.fallbackHandler.WorldFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author NicholasLiu
 * @Date 2018/12/28 17:13
 **/
@FeignClient(value = "PROVIDER", fallbackFactory = WorldFallbackFactory.class)
public interface WorldService {

    @RequestMapping("/aa")
    public String getAA();

}
