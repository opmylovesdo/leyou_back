package com.leyou.api.cart;

import com.leyou.pojo.cart.Cart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/29
 * @Description: com.leyou.api.cart
 * @version: 1.0
 */
@Api("购物车服务接口")
public interface CartControllerApi {

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @ApiOperation("添加购物车")
    @PostMapping
    ResponseEntity<Void> addCart(@RequestBody Cart cart);

    /**
     * 查询购物车列表
     * @return
     */
    @ApiOperation("查询购物车列表")
    @GetMapping
    ResponseEntity<List<Cart>> queryCartList();

    /**
     * 更新购物车商品数量
     * @param cart
     * @return
     */
    @ApiOperation("更新购物车商品数量")
    @PutMapping
    ResponseEntity<Void> updateNum(@RequestBody Cart cart);

    /**
     * 根据skuId删除购物车指定商品
     * @param skuId
     * @return
     */
    @ApiOperation("根据skuId删除购物车指定商品")
    @DeleteMapping("{skuId}")
    ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId);
}
