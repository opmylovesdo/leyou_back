package com.leyou.search.test;

import com.leyou.common.pojo.response.PageResult;
import com.leyou.pojo.item.Sku;
import com.leyou.pojo.item.request.SpuListRequest;
import com.leyou.pojo.item.response.SpuListResponse;
import com.leyou.pojo.search.Goods;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.dao.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/22
 * @Description: com.leyou.search
 * @version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class testES {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void testAddGoodsDocuments() {
        Integer page = 1;
        Integer rows = 200;

        SpuListRequest request = new SpuListRequest();

        do {
            request.setPage(page);
            request.setRows(rows);
            PageResult<SpuListResponse> pageResult = this.goodsClient.querySpuListByPage(page, rows, null, null).getBody();
            List<SpuListResponse> items = pageResult.getItems();

            List<Goods> goodsList = items.stream().map(spuListResponse -> {
                try {
                    return this.searchService.buildGoods(spuListResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());

            goodsRepository.saveAll(goodsList);

            rows = goodsList.size();
            page++;
        }
        while (rows == 100);
    }


    @Test
    public void testGetSpuList(){

        List<SpuListResponse> items = this.goodsClient.querySpuListByPage(1, 100, null, null).getBody().getItems();
        items.stream().forEach(System.out::println);
    }
    
    
    @Test
    public void testGoods(){
        List<Sku> skus = this.goodsClient.querySkuListBySpuId(2L).getBody();
        skus.stream().forEach(System.out::println);
    }
    

}
