package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;

/**
 * @Author：LovingLiu
 * @Description: 异步处理请求
 * @Date：Created in 2019-12-17
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class AsyncController {
    @GetMapping
    public String order() throws Exception{
        log.info("主线程开始");
        Thread.sleep(1000);
        log.info("主线程结束");
        return "success";
    }
    @GetMapping("/orderAsync")
    public Callable<String> orderAsync() throws Exception{
        log.info("主线程开始");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("副线程开始");
                Thread.sleep(1000);// 模拟下单的业务逻辑
                log.info("副线程返回");
                return "success";
            }
        };
        log.info("主线程结束");
        return result;
    }

    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @GetMapping("/orderDeferred")
    public DeferredResult <String> orderDeferred() throws Exception{
        log.info("主线程开始");
        String orderNo = RandomStringUtils.randomNumeric(8); // 生成订单号
        mockQueue.setPlaceOrder(orderNo); // 放入消息队列
        DeferredResult<String> result = new DeferredResult<>();
        deferredResultHolder.getMap().put(orderNo,result);
        log.info("主线程结束");
        return result;
    }
}
