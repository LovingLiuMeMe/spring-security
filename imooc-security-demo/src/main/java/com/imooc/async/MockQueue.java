package com.imooc.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 模拟消息队列
 * @Date：Created in 2019-12-17
 */
@Component
@Slf4j
public class MockQueue {
    private String placeOrder; // 下单的消息
    private String completeOrder; // 订单完成的消息

    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder){
        new Thread(()-> {
            log.info("接到下单请求, {}", placeOrder);
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }

            this.completeOrder = placeOrder;
            log.info("下单请求处理完毕, {}", placeOrder);
        }).start();

    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
