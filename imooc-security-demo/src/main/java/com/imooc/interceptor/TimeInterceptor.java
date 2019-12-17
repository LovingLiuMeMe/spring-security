package com.imooc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description: 时间拦截器 （Interceptor解决的是 无法Spring管理 因为符合的是J2EE的规范, 无法知道控制器信息）
 * @Date：Created in 2019-12-16
 */
@Component
@Slf4j
public class TimeInterceptor implements HandlerInterceptor {
    /**
     * 控制器（Controller）方法调用之前会被调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        log.info("methodClassName=>{}",((HandlerMethod) handler).getBean().getClass().getName());
        log.info("methodName=>{}",((HandlerMethod) handler).getMethod().getName());
        request.setAttribute("startTime", new Date().getTime());
        return true;
    }

    /**
     * 控制器（Controller）方法调用成功之后会被调用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
        Long start = (Long) request.getAttribute("startTime");
        log.info("time interceptor 耗时:{}", new Date().getTime() - start);
    }

    /**
     * 控制器（Controller）方法调用之后会被调用（无论成功或者失败）
     * 注意:@ControllerAdvice声明的统一异常处理是在afterCompletion 之前的。所以发生的异常如果被@ControllerAdvice声明的异常处理类处理,无法捕获。ex 为null
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
        Long start = (Long) request.getAttribute("startTime");
        log.info("afterCompletion 耗时:{}", start);
    }
}
