package com.imooc.security.core.properties;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-23
 */
public class ImageCodeProperties {
    private int width = 67;
    private int height = 23;
    private int length = 4;
    private int expire = 60;
    private String url = ""; // 拦截的请求URL

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

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
