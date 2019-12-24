package com.imooc.security.core.properties;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-23
 */
public class ValidateCodeProperties {
    private ImageCodeProperties image = new ImageCodeProperties();

    public ImageCodeProperties getImage() {
        return image;
    }

    public void setImage(ImageCodeProperties image) {
        this.image = image;
    }
}
