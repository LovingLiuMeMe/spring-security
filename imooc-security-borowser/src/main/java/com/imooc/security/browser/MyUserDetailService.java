package com.imooc.security.browser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @Author：LovingLiu
 * @Description: 实现接口 UserDetails,spring security 会自动读取
 * @Date：Created in 2019-12-18
 */
@Component
public class MyUserDetailService implements UserDetailsService {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("登录用户名:"+username);
        /**
         * 1.此处读取数据库和权限
         * 这里暂时先写死了
         */

        /**
         * 2.查看账户状态
         */
        boolean isEnabled = isEnabled(null);
        boolean isAccountNonExpired = isAccountNonExpired(null);
        boolean isAccountNonLocked = isAccountNonLocked(null);
        boolean isCredentialsNonExpired =  isCredentialsNonExpired(null);

        return new User(username,
                passwordEncoder.encode("12345"),
                isEnabled,
                isAccountNonExpired,
                isCredentialsNonExpired,
                isAccountNonLocked,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

    /**
     * 是否可用（自定义实现）
     * @param arg
     * @return
     */
    public boolean isEnabled(Object arg) {
        return true;
    }

    /**
     * 账号是否过期
     * @param arg
     * @return
     */
    public boolean isAccountNonExpired(Object arg) {
        return true;
    }

    /**
     * 账号是否被冻结
     * @param arg
     * @return
     */
    public boolean isAccountNonLocked(Object arg) {
        return true;
    }

    /**
     * 账号密码是否过期（因为存在密码的有效期为30天的那种高级防护系统）
     * @param arg
     * @return
     */
    public boolean isCredentialsNonExpired(Object arg) {
        return true;
    }
}
