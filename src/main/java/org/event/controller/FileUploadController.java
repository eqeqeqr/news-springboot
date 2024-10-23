package org.event.controller;

import org.event.pojo.Result;
import org.event.service.UserService;
import org.event.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping
    public Result<String> upload(MultipartFile file) throws Exception {
        //把文件的内容存储到本地次哦岸上
        String originalFilename = file.getOriginalFilename();
        //保证文件名字唯一，uuid
        String fileName = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        String mkdir="bigEvent/";
        // file.transferTo(new File("C:\\Users\\qw1500292505\\Desktop\\files\\"+fileName));
        String url = aliOssUtil.uploadFile(mkdir+fileName, file.getInputStream());
        return Result.success(url);
    }
}
