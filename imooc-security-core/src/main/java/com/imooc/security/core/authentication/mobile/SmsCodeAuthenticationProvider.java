package com.imooc.security.core.authentication.mobile;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @Author：LovingLiu
 * @Description: SmsCodeAuthenticationProvider
 * @Date：Created in 2019-12-24
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 身份验证逻辑
     * 通过自定义UserDetails 获取用户信息，重新组装AuthenticationToken
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken smsCodeAuthenticationToken = (SmsCodeAuthenticationToken) authentication;
        /**
         * 此时拿到的是手机号
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername((String)smsCodeAuthenticationToken.getPrincipal());
        if(userDetails == null){
            throw  new InternalAuthenticationServiceException("无法获取用户信息");
        }
        /**
         * 组装认证结果
         */
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails,userDetails.getAuthorities());
        authenticationResult.setDetails(smsCodeAuthenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * 指定 AuthenticationManager 调用具体的Provider执行 (SmsCodeAuthenticationProvider)
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
