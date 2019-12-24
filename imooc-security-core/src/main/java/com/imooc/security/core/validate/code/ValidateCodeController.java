package com.imooc.security.core.validate.code;

import com.imooc.security.core.validate.code.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author：LovingLiu
 * @Description: 获取验证码图片的Controller
 * @Date：Created in 2019-12-22
 */
@RestController
public class ValidateCodeController  {
    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    public static final String SMS_SESSION_KEY = "SESSION_KEY_SMS_CODE";
    // session 操作
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    // 生成验证码逻辑可配置 改造
    @Autowired
    private ValidateCodeGenerator imageCodeGenerator;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1.生成验证码
        ImageCode imageCode = (ImageCode) imageCodeGenerator.generator(request);
        // 2.将随机数存放在Session中
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,imageCode);
        // 3.将图片写回响应
        ImageIO.write(imageCode.getImage(),"JPEG",response.getOutputStream());
    }

    @Autowired
    private SmsCodeSender smsCodeSender;

    /**
     * private ValidateCodeGenerator imageCodeGenerator
     * private ValidateCodeGenerator smsCodeGenerator
     * 都能准确的找到具体的实现类，是因为 imageCodeGenerator 使用的是@Bean
     * smsCodeGenerator 使用的是@Component("smsCodeGenerator")
     *
     */
    @Autowired
    private ValidateCodeGenerator smsCodeGenerator;

    @GetMapping("/code/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        // 1.生成验证码
        ValidateCode smsCode = smsCodeGenerator.generator(request);
        // 2.将随机数存放在Session中
        sessionStrategy.setAttribute(new ServletWebRequest(request),SESSION_KEY,smsCode);

        // 3.调用发送器发送验证码
        String mobile = ServletRequestUtils.getRequiredStringParameter(request,"mobile");

        smsCodeSender.send(mobile,smsCode.getCode());
    }
}
