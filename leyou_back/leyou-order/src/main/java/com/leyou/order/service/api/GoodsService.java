package com.leyou.order.service.api;

import com.leyou.api.item.GoodsControllerApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "leyou-gateway", path = "/api/item")
public interface GoodsService extends GoodsControllerApi {
}
