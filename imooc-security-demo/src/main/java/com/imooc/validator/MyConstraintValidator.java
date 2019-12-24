package com.imooc.validator;

import com.imooc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * @Author：LovingLiu
 * @Description: Spring 会将 实现了ConstraintValidator接口的类自动注入到容器中。不用@Component
 * @Date：Created in 2019-12-16
 */
public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object> {
    /**
     * 设置可以获取 Spring管理的所有的类
     */
    @Autowired
    private HelloService helloService;

    /**
     * 校验器初始化
     * @param constraintAnnotation
     */
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("my validator init...");
    }

    /**
     *
     * @param value 要校验的值
     * @param context 校验的上下文
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        String result = helloService.sayHello(value.toString());
        System.out.println("isValid:"+result);
        return false;// 是否通过验证
    }
}
