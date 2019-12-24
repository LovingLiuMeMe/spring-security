package com.imooc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@Controller
public class DemoControlleer {

    @GetMapping("/hello")
    public String hello(){
        return "login";
    }
}
