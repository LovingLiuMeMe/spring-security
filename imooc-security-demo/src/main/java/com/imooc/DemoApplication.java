package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author：LovingLiu
 * @Description: 替换 security.basic.enabled （因为该配置被弃用）
 * @Date：Created in 2019-12-15
 */
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args){
        SpringApplication.run(DemoApplication.class, args);
    }
}
