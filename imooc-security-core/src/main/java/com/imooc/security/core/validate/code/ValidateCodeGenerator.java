package com.imooc.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author：LovingLiu
 * @Description: 自定义生成验证码的逻辑
 * @Date：Created in 2019-12-23
 */
public interface ValidateCodeGenerator {
    ValidateCode generator(HttpServletRequest request);
}
