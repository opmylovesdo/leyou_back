package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.pojo.upload.response.UploadResponse;
import com.leyou.upload.config.ContentTypeConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.upload.service
 * @version: 1.0
 */
@Service
public class UploadService {

    @Autowired
    private ContentTypeConfiguration configuration;

    @Autowired
    private FastFileStorageClient storageClient;

    public static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);


    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    public UploadResponse upload(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        //判断文件类型
        String contentType = file.getContentType();
        if (!configuration.getTypes().contains(contentType)) {
            //文件类型不合法
            LOGGER.info("文件类型不合法:{}", originalFilename);
            return new UploadResponse();
        }

        try {
            //判断文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                LOGGER.info("文件内容不合法:{}", originalFilename);
                return new UploadResponse();
            }

            //文件上传, 保存到服务器
            //file.transferTo(new File("D:\\leyou\\img\\" + originalFilename));
            String ext = StringUtils.substringAfterLast(originalFilename, ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);

            //生成文件url地址 返回
            return new UploadResponse(configuration.getBasePath() + storePath.getFullPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new UploadResponse();
    }
}
