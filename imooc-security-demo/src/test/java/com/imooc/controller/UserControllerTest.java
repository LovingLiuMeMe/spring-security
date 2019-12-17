package com.imooc.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    /**
     * 在所有方法执行之前执行
     * 构造mvc环境
     */
    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void whenQuerySuccess() throws Exception {
        // 构建http请求
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user") // 请求地址
                .param("userName","jiaojiao")
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // content-type: application/json
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期待的请求结果状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))// 期待的请求结果长度为 3
                .andReturn().getResponse().getContentAsString();
        log.info("【result】=>{}",result);
    }

    /**
     * 正常查询
     * @throws Exception
     */
    @Test
    public void whenGetInfoSuccess() throws Exception {
        // 构建http请求
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1") // 请求地址
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // content-type: application/json
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期待的请求结果状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("lovingliu"))// 期待的请求结果长度为 3
                .andReturn().getResponse().getContentAsString();
        log.info("【result】=>{}",result);
    }

    /**
     * 获取用户的信息（用户Id数据类型错误的条件下）
     * @throws Exception
     */
    @Test
    public void whenGetInfoFail() throws Exception {
        // 构建http请求
        mockMvc.perform(MockMvcRequestBuilders.get("/user/a") // 请求地址
                .contentType(MediaType.APPLICATION_JSON_UTF8)) // content-type: application/json
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()); // 期待的请求结果状态码 200
    }

    /**
     * 测试用户的创建
     * @throws Exception
     */
    @Test
    public void whenCreateSuccess() throws Exception {
        Date date = new Date();
        System.out.println(date.getTime());
        String content = "{\"username\": \"lovingliu\",\"birthday\":\""+date.getTime()+"\"}";
        // 构建http请求
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user") // 请求地址
                .contentType(MediaType.APPLICATION_JSON_UTF8) // content-type: application/json
                .content(content)) // 添加请求内容
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期待的请求结果状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        log.info("【result】=>{}",result);
    }

    /**
     * 测试用户的修改 put请求
     * @throws Exception
     */
    @Test
    public void whenUpdateSuccess() throws Exception {
        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        System.out.println(date.getTime());
        String content = "{\"id\": 1,\"username\": \"lovingliu\",\"password\": \"12345\",\"birthday\":\""+date.getTime()+"\"}";
        // 构建http请求
        String result = mockMvc.perform(MockMvcRequestBuilders.put("/user") // 请求地址
                .contentType(MediaType.APPLICATION_JSON_UTF8) // content-type: application/json
                .content(content)) // 添加请求内容
                .andExpect(MockMvcResultMatchers.status().isOk()) // 期待的请求结果状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();
        log.info("【result】=>{}",result);
    }
    /**
     * 测试用户的删除
     * @throws Exception
     */
    @Test
    public void whenDeleteSuccess() throws Exception {
        // 构建http请求
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 测试图片的上传
     */
    @Test
    public void whenUploadSuccess() throws Exception {
        // 构建http请求
        String result = mockMvc.perform(MockMvcRequestBuilders.fileUpload("/file")
                        .file(new MockMultipartFile("file","test.txt","multipart/form-date","hello upload".getBytes("UTF-8"))))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString();
        log.info("【result】=>{}",result);
    }
}