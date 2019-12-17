package com.imooc.config;

import com.imooc.filter.TimeFilter2;
import com.imooc.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description: 不继承Filter接口 实现Filter拦截器(可适用于第三方Filter)
 * @Date：Created in 2019-12-16
 */
@Configuration
public class WebCongfig extends WebMvcConfigurationSupport {
    @Autowired
    private TimeInterceptor timeInterceptor;

    /**
     * 配置异步请求 Callable 和 DeferredResult 参数
     * @param configurer
     */
    @Override
    protected void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(20000);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeInterceptor);
    }
    /**
     * Filter 设置
     * @return
     */
    @Bean
    public FilterRegistrationBean timeFilter2() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        TimeFilter2 timeFilter2 = new TimeFilter2();
        registrationBean.setFilter(timeFilter2);
        List<String> urls = new ArrayList<>();
        urls.add("/*"); // 指定拦截的url列表
        registrationBean.setUrlPatterns(urls);
        return registrationBean;
    }
}
