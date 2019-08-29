package com.leyou.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/26
 * @Description: com.leyou.user.test
 * @version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest2 {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired

    @Test
    public void testRedisTemplate() {
        redisTemplate.opsForList().leftPushAll("names", "a", "b", "c", "d");
        Long size = redisTemplate.opsForList().size("names");
        System.out.println(size);
        List<String> names = redisTemplate.opsForList().range("names", 0, size);
        names.forEach(System.out::println);
    }

}
