package com.imooc.service.impl;

import com.imooc.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-16
 */
@Service
@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        log.info("sayHello！！！");
        return "hello,"+name;
    }
}
