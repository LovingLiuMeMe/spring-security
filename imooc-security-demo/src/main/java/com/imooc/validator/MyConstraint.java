package com.imooc.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author：LovingLiu
 * @Description: 自定义参数校验逻辑
 * @Date：Created in 2019-12-16
 */
@Target({ElementType.FIELD,ElementType.METHOD}) // 可以添加在什么类型上
@Retention(RetentionPolicy.RUNTIME) // 起作用的时机
@Constraint(validatedBy = MyConstraintValidator.class) // 实现的逻辑（要执行的校验逻辑）
public @interface MyConstraint {
    /**
     * 自定义校验器必须含有以下三个属性
     */
    String message() default "自定义校验逻辑验证未通过";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
