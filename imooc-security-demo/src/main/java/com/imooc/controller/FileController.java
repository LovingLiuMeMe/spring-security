package com.imooc.controller;


import com.imooc.dto.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

/**
 * @Author：LovingLiu
 * @Description:
 * @Date：Created in 2019-12-17
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    private static String folder = "/Users/lovingliu/Desktop/project/imooc-security/imooc-security-demo/src/main/java/com/imooc/controller";
    /**
     * 文件的上传
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping
    public FileInfo upload(MultipartFile file) throws Exception{
        log.info("filename: {}", file.getName());
        log.info("OriginalFilename: {}",file.getOriginalFilename());
        log.info("fileSize: {}",file.getSize());

        File localFile = new File(folder, new Date().getTime()+".txt");
        file.transferTo(localFile);

        return new FileInfo(localFile.getAbsolutePath());
    }

    @GetMapping("/{id}")
    public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception {

        try(
            InputStream inputStream = new FileInputStream(new File(folder,id+".txt"));
            OutputStream outputStream = response.getOutputStream();
        ){
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition","attachment;filename=test.txt");

            IOUtils.copy(inputStream, outputStream);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
