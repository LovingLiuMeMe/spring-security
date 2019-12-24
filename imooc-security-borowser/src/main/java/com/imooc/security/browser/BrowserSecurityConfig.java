package com.imooc.security.browser;

import com.imooc.security.core.authentication.mobile.SmsCodeAuthenticationConfig;
import com.imooc.security.core.properties.SecurityProperties;
import com.imooc.security.core.validate.code.ValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @Author：LovingLiu
 * @Description: Security安全配置的适配器
 * @Date：Created in 2019-12-18
 */
@Configuration
@EnableWebSecurity
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler imoocAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 推荐使用 BCrypt 的加密的形式
    }

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyUserDetailService userDetailService;

    // 引入短信登录的配置类
    private SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // tokenRepository.setCreateTableOnStartup(true); // 系统启动时自动创建表（仅仅限制第一次系统启动,启动一次之后注解掉）
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet(); // 设置配置的属性

        // 短信验证码过滤器
        ValidateCodeFilter smsCodeFilter = new ValidateCodeFilter();
        smsCodeFilter.setAuthenticationFailureHandler(imoocAuthenticationFailureHandler);
        smsCodeFilter.setSecurityProperties(securityProperties);
        smsCodeFilter.afterPropertiesSet(); // 设置配置的属性

        http.addFilterAfter(smsCodeFilter, UsernamePasswordAuthenticationFilter.class).
                addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class). // 添加自定义过滤器
                formLogin(). // 声明验证方式为表单登录
                    loginPage("/authentication/require"). // 自定义登录界面
                    loginProcessingUrl("/authentication/form"). // 指定接收username/password的请求路径
                    successHandler(imoocAuthenticationSuccessHandler). // 使用自定义成功处理器
                    failureHandler(imoocAuthenticationFailureHandler). // 使用自定义失败处理器
                    and(). //
                rememberMe().
                    tokenRepository(persistentTokenRepository()). // 返回jdbcTokenRepository的实现
                    tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds()). // 配置有效时间
                    userDetailsService(userDetailService). // 使用声明的用户
                and().
                    authorizeRequests(). // 对请求进行授权
                    antMatchers("/authentication/require","/authentication/form","/code/*",securityProperties.getBrowser().getLoginPage()).permitAll().// 配置不进行认证请求
                    anyRequest(). // 任意请求
                    authenticated(). // 都需要身份认证
                and().
                    csrf().disable(). // 关闭跨站请求防护
                    apply(smsCodeAuthenticationConfig);// 相当于添加都后面
    }
}