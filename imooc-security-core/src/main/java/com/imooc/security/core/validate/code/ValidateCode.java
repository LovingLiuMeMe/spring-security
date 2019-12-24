package com.imooc.security.core.validate.code;

import java.time.LocalDateTime;

/**
 * @Author：LovingLiu
 * @Description:
 * 图片验证码实体类
 * 注意: LocalDateTime 的使用
 * @Date：Created in 2019-12-22
 */
public class ValidateCode {
    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
