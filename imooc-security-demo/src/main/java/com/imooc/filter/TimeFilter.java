package com.imooc.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author：LovingLiu
 * @Description: 自定义过滤器
 * @Date：Created in 2019-12-16
 */
//@Component
@Slf4j
public class TimeFilter implements Filter {
    /**
     * 指定过滤器逻辑
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("time filter start");
        long start = System.currentTimeMillis();
        chain.doFilter(request,response);
        log.info("filter spend time {}",System.currentTimeMillis() - start);
        log.info("time filter finish");
    }

    /**
     * 初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter init");
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        log.info("time filter destory");
    }
}
