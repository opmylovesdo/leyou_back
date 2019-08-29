package com.leyou.cart.controller;

import com.leyou.api.cart.CartControllerApi;
import com.leyou.cart.service.CartService;
import com.leyou.pojo.cart.Cart;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/29
 * @Description: com.leyou.cart.controller
 * @version: 1.0
 */
@RestController
public class CartController implements CartControllerApi {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     *
     * @param cart
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        this.cartService.addCart(cart);
        return ResponseEntity.ok().build();
    }


    /**
     * 查询购物车列表
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        List<Cart> carts = this.cartService.queryCartList();
        if(CollectionUtils.isEmpty(carts)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 更新购物车商品数量
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateCarts(cart);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据skuId删除购物车指定商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") Long skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
