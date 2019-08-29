package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.gateway
 * @version: 1.0
 */
@Configuration
public class LeyouCorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //1.添加CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //1)允许的域
        config.addAllowedOrigin("http://manage.leyou.com");
        //2)是否发送cookie信息
        config.setAllowCredentials(true);
        //3)允许的请求方式
        config.addAllowedMethod("*");
        //4)允许的请求方式
        config.addAllowedHeader("*");


        //2. 添加映射路径, 拦截一切请求
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", config);

        return new CorsFilter(configurationSource);
    }
}
