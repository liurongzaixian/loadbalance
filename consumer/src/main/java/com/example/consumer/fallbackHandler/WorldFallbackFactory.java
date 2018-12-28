package com.example.consumer.fallbackHandler;

import com.example.consumer.service.WorldService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @Author NicholasLiu
 * @Date 2018/12/28 17:15
 **/
@Component
public class WorldFallbackFactory implements FallbackFactory<WorldService>{

    @Override
    public WorldService create(Throwable throwable) {
        return new WorldService(){
            @Override
            public String getAA() {
                return "服务器开小差了..............................";
            }
        };
    }
}
