package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author：LovingLiu
 * @Description: 解决配置过时的问题
 * @Date：Created in 2019-12-15
 */
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
    }
    /**
     * 配置一个userDetailsService Bean不在生成默认security.user用户
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }
}
