package com.leyou.search.client;

import com.leyou.api.item.GoodsControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/22
 * @Description: com.leyou.search.client
 * @version: 1.0
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsControllerApi {


}
