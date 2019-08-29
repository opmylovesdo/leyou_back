package com.leyou.cart.client;

import com.leyou.api.item.GoodsControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/29
 * @Description: com.leyou.cart.client
 * @version: 1.0
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsControllerApi {
}
