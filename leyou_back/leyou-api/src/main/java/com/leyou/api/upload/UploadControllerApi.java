package com.leyou.api.upload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.api.item
 * @version: 1.0
 */
@Api("图片上传接口")
@RequestMapping("upload")
public interface UploadControllerApi {

    /**
     * 图片上传
     * @param file
     * @return
     */
    @ApiOperation("图片上传")
    @RequestMapping("image")
    ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file);
}
