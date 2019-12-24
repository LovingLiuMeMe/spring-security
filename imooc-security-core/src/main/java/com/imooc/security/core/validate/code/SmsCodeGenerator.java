package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description: 短信验证码生成器
 * @Date：Created in 2019-12-23
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generator(HttpServletRequest request){
        String code = RandomStringUtils.randomNumeric(securityProperties.getSms().getLength());
        return new ValidateCode(code, securityProperties.getSms().getExpire());
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
