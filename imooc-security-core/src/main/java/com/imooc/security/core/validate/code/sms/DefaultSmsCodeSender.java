package com.imooc.security.core.validate.code.sms;

/**
 * @Author：LovingLiu
 * @Description: 默认实现短信验证码的发送
 * @Date：Created in 2019-12-23
 */
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String mobile, String code) {
        // 调用短信服务商发送信息
        System.out.println("向手机: "+mobile+", 发送验证码: "+code);
    }
}
