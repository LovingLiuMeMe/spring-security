package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：LovingLiu
 * @Description: 生成验证码逻辑的相关配置
 * @Date：Created in 2019-12-23
 */
@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @ConditionalOnMissingBean注解作用在@bean定义上
     * 可以给该注解传入参数例如@ConditionOnMissingBean(name = "example")
     * 这个表示如果name为“example”的bean存在，这该注解修饰的代码块不执行
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    /**
     * @ConditionalOnMissingBean
     * 指定接口,这个表示如果没有实现该接口的类，就执行下面的代码
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender(){
        return new DefaultSmsCodeSender();
    }
}
