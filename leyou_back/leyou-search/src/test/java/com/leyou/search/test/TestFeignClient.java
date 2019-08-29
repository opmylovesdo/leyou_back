package com.leyou.search.test;

import com.leyou.pojo.item.Brand;
import com.leyou.pojo.item.request.BrandListRequest;
import com.leyou.search.client.BrandClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/22
 * @Description: com.leyou.search.test
 * @version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeignClient {

    @Autowired
    private BrandClient brandClient;


    @Test
    public void testQueryBrandByCid(){
        List<Brand> brandList = this.brandClient.queryBrandByCid(76L).getBody();
        brandList.stream().forEach(System.out::println);
    }

}
