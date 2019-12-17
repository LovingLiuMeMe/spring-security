package com.imooc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.imooc.dto.User;
import com.imooc.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    /**
     * 查询用户列表
     * @param userName
     * @return
     */
    @GetMapping
    @JsonView(User.UserSimpleView.class)
    public List<User> user(@RequestParam String userName){
        log.info("【userName】{}",userName);
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        userList.add(new User());
        return userList;
    }

    /**
     * 查询用户指定信息
     * @param id
     * @return
     */
    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@PathVariable("id") String id){
        log.info("【id】=>{}", id);
        User user = new User();
        user.setUsername("lovingliu");
        user.setPassword("123");
        return user;
    }

    @PostMapping
    @JsonView(User.UserDetailView.class)
    public User create(@Valid @RequestBody User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach(error -> log.error("【error】=>{}",error.getDefaultMessage()));
        }
        log.info("【user】=>{}",user);
        user.setId("1");
        return user;
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @PutMapping
    @JsonView(User.UserDetailView.class)
    public User update(@Valid @RequestBody User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().stream().forEach(error -> {
                FieldError fieldError = (FieldError) error;
                String message = "{} => {}";
                log.error(message,fieldError.getField(),error.getDefaultMessage());
            });
        }
        log.info("【user】=>{}",user);
        return user;
    }

    @DeleteMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public void delete(@PathVariable("id") String id){
        log.info("【id】=>{}",id);
    }

    @GetMapping("/testExec")
    public void testExec(){
        throw new UserException("用户不存在",123);
    }
}
