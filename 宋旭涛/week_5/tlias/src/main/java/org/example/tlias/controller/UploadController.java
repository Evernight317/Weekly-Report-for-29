package org.example.tlias.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.tlias.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequestMapping
public class UploadController {
    @PostMapping("/upload")
    public Result upload(String username, Integer age, MultipartFile image) {
        log.info("username: {}, age: {}, image: {}", username, age, image);
        try {
            image.transferTo(new File("C:\\Users\\GuMing\\Desktop\\临时文件\\" + image.getOriginalFilename()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}
