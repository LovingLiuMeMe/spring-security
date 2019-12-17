package com.imooc.dto;

import lombok.Data;

/**
 * @Author：LovingLiu
 * @Description: 文件信息
 * @Date：Created in 2019-12-17
 */
@Data
public class FileInfo {
    public FileInfo(String path){
        this.path = path;
    }
    private String path;
}
