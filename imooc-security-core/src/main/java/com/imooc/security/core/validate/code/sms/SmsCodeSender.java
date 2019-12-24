package com.imooc.security.core.validate.code.sms;

/**
 * @Author：LovingLiu
 * @Description: 短信验证码的发送接口
 * @Date：Created in 2019-12-23
 */
public interface SmsCodeSender {
    void send(String mobile, String code);
}
