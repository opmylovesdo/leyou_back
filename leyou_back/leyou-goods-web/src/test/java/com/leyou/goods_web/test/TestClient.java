package com.leyou.goods_web.test;

import com.leyou.page.client.GoodsClient;
import com.leyou.pojo.item.Sku;
import com.leyou.pojo.item.Spu;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.goods_web.test
 * @version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void testClient(){
        Spu spu = goodsClient.querySpuById(57L).getBody();
        System.out.println(spu);
    }

}
