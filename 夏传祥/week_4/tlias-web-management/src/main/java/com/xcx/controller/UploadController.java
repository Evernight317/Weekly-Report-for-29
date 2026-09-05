package com.xcx.controller;

import com.xcx.pojo.Result;
import com.xcx.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Autowired
    private AliOSSUtils aliOSSUtils;
    //本地存储
    /*@PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile image) throws Exception {
        log.info("{},{},{}",username,age,image);
        //获取原始文件名
        String original=image.getOriginalFilename();

        //构造唯一文件名-uuid
        int index=original.lastIndexOf(".");
        String ext=original.substring(index);
        String uuid= UUID.randomUUID().toString();
        String newName=uuid+ext;

        //保存图片
        image.transferTo(new File("C:\\Users\\14095\\Pictures\\Camera Roll\\"+ newName));
        return Result.success();
    }*/

    //阿里云 OSS
    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws Exception {
        log.info("文件上传,{}",image);

        //调用阿里云 OSS 工具类,new 一个对象或者ioc容器
        String url=aliOSSUtils.upload(image);
        log.info("文件上传完成,{}",url);
        return Result.success(url);
    }
}
