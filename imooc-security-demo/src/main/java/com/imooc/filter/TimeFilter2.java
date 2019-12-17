package com.imooc.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author：LovingLiu
 * @Description: 自定义过滤器
 * @Date：Created in 2019-12-16
 */
@Slf4j
public class TimeFilter2 implements Filter {
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
        log.info("time filter2 start");
        long start = System.currentTimeMillis();
        chain.doFilter(request,response);
        log.info("filter2 spend time {}",System.currentTimeMillis() - start);
        log.info("time filter2 finish");
    }

    /**
     * 初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("time filter2 init");
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {
        log.info("time filter2 destory");
    }
}
