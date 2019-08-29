package com.leyou.page.service;

import com.leyou.page.client.BrandClient;
import com.leyou.page.client.CategoryClient;
import com.leyou.page.client.GoodsClient;
import com.leyou.page.client.SpecificationClient;
import com.leyou.pojo.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.page.service
 * @version: 1.0
 */
@Service
public class GoodsWebService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    /**
     * spu信息             spu
     * spu的详情           spuDetail
     * spu下的所有sku      skus
     * 品牌               brand
     * 商品三级分类        categories [{id:name}]
     * 商品规格参数        paramMap [{id:name}] [{id: [白色, 金色]}]
     * 规格参数组          groups []
     *
     * @param spuId
     * @return
     */
    public Map<String, Object> loadData(Long spuId) {
        //封装数据
        Map<String, Object> dataMap = new HashMap<>();

        // 根据id查询spu
        Spu spu = this.goodsClient.querySpuById(spuId).getBody();
        // spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId).getBody();
        // skus
        List<Sku> skus = this.goodsClient.querySkuListBySpuId(spuId).getBody();
        // brand
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId()).getBody();
        // categories [{id:name}]
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Long> ids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryCategoryNamesByIds(ids).getBody();
        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }

        // groups []
        List<SpecGroup> groups = this.specificationClient.querySpecGroupsWithParamsByCid(spu.getCid3()).getBody();

        // 查询特殊的规格参数
        // paramMap [{id:name}] [{id: [白色, 金色]}]

        List<SpecParam> params = this.specificationClient.querySpecParams(null, spu.getCid3(), false, null).getBody();
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });


        //封装spu
        dataMap.put("spu", spu);
        //封装spuDetail
        dataMap.put("spuDetail", spuDetail);
        //封装sku集合
        dataMap.put("skus", skus);
        //分类
        dataMap.put("categories", categories);
        //品牌
        dataMap.put("brand", brand);
        //规格参数组
        dataMap.put("groups", groups);
        //查询特殊规格参数
        dataMap.put("paramMap", paramMap);

        return dataMap;

    }
}
