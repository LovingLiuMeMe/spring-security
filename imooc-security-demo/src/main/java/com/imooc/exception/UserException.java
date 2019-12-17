package com.imooc.exception;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 自定义异常
 * @Date：Created in 2019-12-16
 */
@Data
public class UserException extends RuntimeException {

    private Integer id;
    public UserException(String msg,Integer id){
        super(msg);
        this.id = id;
    }
}
