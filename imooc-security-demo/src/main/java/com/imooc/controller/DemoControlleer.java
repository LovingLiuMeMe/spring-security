package com.imooc.controller;

import com.imooc.dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@RestController
public class DemoControlleer {

    @GetMapping("/hello")
    public List<User> hello(){
        return null;
    }
}
