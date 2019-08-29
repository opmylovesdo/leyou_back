//package com.leyou.item;
package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/10
 * @Description: com.leyou
 * @version: 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.item.mapper")
//@EntityScan("com.leyou.pojo.item")//扫描实体类
//@ComponentScan(basePackages={"com.leyou.api"})//扫描接口
//@ComponentScan(basePackages={"com.leyou.item"})//扫描本服务的类
//@ComponentScan(basePackages={"com.leyou.common"})//扫描common下的所有类
public class LeyouItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeyouItemApplication.class, args);
    }
}
