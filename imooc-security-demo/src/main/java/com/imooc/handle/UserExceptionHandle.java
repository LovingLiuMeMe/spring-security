package com.imooc.handle;

import com.imooc.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author：LovingLiu
 * @Description: 同一异常
 * @Date：Created in 2019-12-16
 */
@ControllerAdvice
public class UserExceptionHandle {
     @ExceptionHandler(value = UserException.class)
     @ResponseBody
     @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
     public Map<String,Object> handleUserException(UserException e){
         Map<String,Object> result = new HashMap<>();
         result.put("id",e.getId());
         result.put("message",e.getMessage());
         return result;
     }
}
