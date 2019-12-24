package com.imooc.code;

import com.imooc.security.core.validate.code.ImageCode;
import com.imooc.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * @Author：LovingLiu
 * @Description: 自定义生成验证码的逻辑
 * @Component("imageCodeGenerator") 覆盖默认的实现
 * @Date：Created in 2019-12-23
 */
// @Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode generator(HttpServletRequest request) {
        System.out.println("更高级的图形验证码的生成代码");
        int width = ServletRequestUtils.getIntParameter(request,"width",150);
        int height = ServletRequestUtils.getIntParameter(request,"height",100);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        return new ImageCode(image, "1111", 60);
    }
}
