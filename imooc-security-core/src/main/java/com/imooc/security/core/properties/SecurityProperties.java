package com.imooc.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-22
 */
@ConfigurationProperties(prefix = "imooc.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
    private ValidateCodeProperties code = new ValidateCodeProperties();
    private SmsCodeProperties sms = new SmsCodeProperties();

    public BrowserProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserProperties browser) {
        this.browser = browser;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }
}
