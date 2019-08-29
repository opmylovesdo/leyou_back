package com.leyou.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.upload.config
 * @version: 1.0
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "upload")
@Data
public class ContentTypeConfiguration {

    private List<String> types = new ArrayList<>();

    private String basePath;
}
