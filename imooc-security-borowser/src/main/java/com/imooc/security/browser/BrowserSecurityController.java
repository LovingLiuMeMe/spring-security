package com.imooc.security.browser;

import com.imooc.security.browser.support.SimpleResponse;
import com.imooc.security.core.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author：LovingLiu
 * @Description: Security安全配置的适配器
 * @Date：Created in 2019-12-18
 */
@RestController
public class BrowserSecurityController{
    private Logger log= LoggerFactory.getLogger(BrowserSecurityController.class);
    private RequestCache requestCache = new HttpSessionRequestCache();// 会将http请求缓存起来
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;
    /**
     * 当需要身份认证时 跳转到这里
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response){
        SavedRequest savedRequest = requestCache.getRequest(request,response);
        if(savedRequest != null){
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("【引发跳转的请求的是:{}】",targetUrl);
            // 引发跳转的请求是已Html结尾的,直接跳转到登录页面
            if(StringUtils.endsWithIgnoreCase(targetUrl,".html")){
                try{
                    redirectStrategy.sendRedirect(request,response,securityProperties.getBrowser().getLoginPage());// 跳转用户自定义页面
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        // 非Html请求 返会401状态码 和 相应的信息
        return new SimpleResponse(401,"访问的服务需要身份信息,请引导用户到登录页面");
    }
}
