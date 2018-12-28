package com.example.providerb.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author NicholasLiu
 * @Date 2018/12/28 15:24
 **/
@RestController
public class HelloController {

    @RequestMapping("/aa")
    public String aa () {
        return "bb";
    }

}
