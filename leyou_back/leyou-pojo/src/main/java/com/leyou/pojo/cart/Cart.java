package com.leyou.pojo.cart;

import lombok.Data;

@Data
public class Cart {
    //Map<userId, Map<skuId, cart>>
    //为方便后台数据展示 直接map.values 获取购物车集合
    private Long userId;// 用户id
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 加入购物车时的价格
    private Integer num;// 购买数量
    private String ownSpec;// 商品规格参数

}