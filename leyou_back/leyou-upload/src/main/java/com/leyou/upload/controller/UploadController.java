package com.leyou.upload.controller;

import com.leyou.api.upload.UploadControllerApi;
import com.leyou.pojo.upload.response.UploadResponse;
import com.leyou.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.upload
 * @version: 1.0
 */
@RestController
@RequestMapping("upload")
public class UploadController implements UploadControllerApi {

    @Autowired
    private UploadService uploadService;


    /**
     * 图片上传
     * @param file
     * @return
     */
    @Override
    @RequestMapping("image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        UploadResponse response = this.uploadService.upload(file);
        String url = response.getUrl();
        if(StringUtils.isBlank(url)){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response.getUrl());
    }
}
