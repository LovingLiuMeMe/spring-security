package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 解决Inteceptor无法拿到请求参数（User user）user无法拿到
 * @Date：Created in 2019-12-16
 */
//@Aspect
//@Component
@Slf4j
public class TimeAspect {
    @Pointcut("execution(public * com.imooc.controller.UserController.* (..))")
    public void timePointcut() {}

    @Around("timePointcut()")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable{
        log.info("time aspect start");
        Object[] args = pjp.getArgs();// 调用被拦击的控制器的方法时 传递的参数
        for (Object obj: args) {
            log.info("arg : {}", obj);
        }
        long start = new Date().getTime();
        Object result = pjp.proceed();// 调用被拦击的控制器的方法
        log.info("result: {}",result);
        log.info("Aspect 耗时: {}",(new Date().getTime() - start));
        log.info("time aspect end");
        return result;
    }
}
