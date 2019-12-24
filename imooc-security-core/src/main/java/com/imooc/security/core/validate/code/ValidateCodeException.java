package com.imooc.security.core.validate.code;


import org.springframework.security.core.AuthenticationException;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-22
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
