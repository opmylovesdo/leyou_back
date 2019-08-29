package com.leyou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/27
 * @Description: com.leyou.gateway.config
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "leyou.filter")
public class FilterProperties {

    private List<String> allowPaths;
}
