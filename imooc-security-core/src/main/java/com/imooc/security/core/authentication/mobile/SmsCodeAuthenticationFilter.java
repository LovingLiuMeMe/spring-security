package com.imooc.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author：LovingLiu
 * @Description: 短信登录的拦截器
 * @Date：Created in 2019-12-24
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String IMOOC_FORM_MOBILE_KEY = "mobile";

    private String mobileParameter = IMOOC_FORM_MOBILE_KEY; // 请求参数名称

    private boolean postOnly = true;

    /**
     * 请求匹配器(登录页面 表单提交请求)
     */
    public SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }

    /**
     * 具体的认证流程
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);


        if (mobile == null) {
            mobile = "";
        }

        mobile = mobile.trim();
        /**
         * 实例化 TOKEN
         */
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);
        /**
         * 将请求的信息 设置到验证请求信息中
         */
        setDetails(request, authRequest);
        /**
         * 进行AuthenticationManager的调用
         */
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(mobileParameter);
    }

    protected void setDetails(HttpServletRequest request,
                              SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "mobile parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }
}

