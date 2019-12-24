package com.imooc.security.core.properties;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-22
 */
public class BrowserProperties {
    private String loginPage = "/imooc-signIn.html"; // 指定默认跳转页面

    private LoginType loginType = LoginType.JSON; // 指定登录方式

    private int rememberMeSeconds = 3600; // 记住我的有效期

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }
}
