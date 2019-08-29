package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
public class LeyouUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUploadApplication.class, args);
    }
}
