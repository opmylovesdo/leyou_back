package com.leyou.pojo.item.request;

import com.leyou.pojo.item.Sku;
import com.leyou.pojo.item.SpuDetail;
import com.leyou.pojo.item.response.SpuListResponse;
import lombok.Data;

import java.util.List;

/**
 * 保存添加商品数据用
 * @Auther: LUOLUO
 * @Date: 2019/8/19
 * @Description: com.leyou.pojo.item.request
 * @version: 1.0
 */
@Data
public class GoodsSaveRequest extends SpuListResponse {

    //商品详情
    private SpuDetail spuDetail;
    //sku列表
    List<Sku> skus;

}
