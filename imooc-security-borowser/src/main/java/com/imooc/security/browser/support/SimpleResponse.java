package com.imooc.security.browser.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse {
    private Integer code;
    private String msg;
}
