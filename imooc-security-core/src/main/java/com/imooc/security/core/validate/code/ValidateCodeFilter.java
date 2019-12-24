package com.imooc.security.core.validate.code;

import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author：LovingLiu
 * @Description:
 * 自定义验证码的过滤器,根据请求判断是哪一种验证
 * OncePerRequestFilter保证每次请求只被调用一次
 * InitializingBean接口的实现方法afterPropertiesSet()会在bean在初始化后自动调用且在init-method之前调用
 * @Date：Created in 2019-12-22
 */
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    private Logger log = LoggerFactory.getLogger(getClass());

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    private Set<String> urls = new HashSet<>(); // 可配置的可拦截的请求URL

    private SecurityProperties securityProperties; // 由Set方法传入

    private AntPathMatcher antPathMatcher = new AntPathMatcher(); // 工具类

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        String[] configUrls = StringUtils.split(securityProperties.getCode().getImage().getUrl(),",");
        for (String config: configUrls) {
            urls.add(config);
            log.info("【afterPropertiesSet自动调用】=> {}",config);
        }
        urls.add("/authentication/form");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url: urls) {
            if(antPathMatcher.match(url,request.getRequestURI())){
                action = true;
            }
        }
        if(action){
            try{
                valicate(new ServletWebRequest(request));
            }catch (ValidateCodeException e){
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;// 直接返回
            }

        }
        //继续执行沿着Filter链执行
        filterChain.doFilter(request, response);
    }

    private void valicate(ServletWebRequest request) throws ServletRequestBindingException {
        // 从Session中获取Token
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request,ValidateCodeController.SESSION_KEY);

        // 获取请求参数
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    "imageCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }

        sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }
}
