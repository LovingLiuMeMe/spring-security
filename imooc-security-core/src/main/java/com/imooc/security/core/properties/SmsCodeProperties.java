package com.imooc.security.core.properties;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-23
 */
public class SmsCodeProperties {
    private int length = 4;
    private int expire = 300;
    private String url = ""; // 拦截的请求URL

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
