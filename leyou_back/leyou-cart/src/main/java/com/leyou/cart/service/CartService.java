package com.leyou.cart.service;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.common.pojo.auth.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.pojo.cart.Cart;
import com.leyou.pojo.item.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/29
 * @Description: com.leyou.cart.service
 * @version: 1.0
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private static final String KEY_PREFIX = "leyou:cart:uid:";

    /**
     * 添加购物车
     * @param cart
     */
    public void addCart(Cart cart) {
        //获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        //拼接redis的key
        String key = KEY_PREFIX + user.getId();
        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean bool = hashOps.hasKey(skuId.toString());
        if(bool){
            //存在, 获取购物车数据
            String json = hashOps.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            //修改购物车skuId对应商品数量
            cart.setNum(cart.getNum() + num);
        }else{
            //不存在, 新增购物车数据
            cart.setUserId(user.getId());
            //其他商品消息, 需要查询商品服务
            Sku sku = goodsClient.querySkuById(cart.getSkuId()).getBody();
            String images = sku.getImages();
            cart.setImage(StringUtils.isBlank(images) ? "" : StringUtils.split(images, ",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(sku.getTitle());
            cart.setPrice(sku.getPrice());
        }
        //将购物车重新写回redis
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车列表
     * @return
     */
    public List<Cart> queryCartList() {
        //获取登录的用户
        UserInfo user = LoginInterceptor.getLoginUser();
        //拼接redis的key
        String key = KEY_PREFIX + user.getId();
        if(!this.redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        //判断是否有数据
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }

        //查询购物车数据
        return carts.stream()
                .map(((o -> JsonUtils.parse(o.toString(), Cart.class))))
                .collect(Collectors.toList());
    }

    /**
     * 更新购物车商品数量
     * @param cart
     */
    public void updateCarts(Cart cart) {
        //获取登录消息
        UserInfo user = LoginInterceptor.getLoginUser();
        //拼接redis的key
        String key = KEY_PREFIX + user.getId();
        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //获取购物车消息
        String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
        Cart cart1 = JsonUtils.parse(cartJson, Cart.class);
        //更新数量
        cart1.setNum(cart.getNum());
        //写入购物车
        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart1));
    }

    /**
     * 根据skuId删除购物车指定商品
     * @param skuId
     * @return
     */
    public void deleteCart(Long skuId) {
        //获取用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        //拼接redis的key
        String key = KEY_PREFIX + user.getId();
        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //删除购物车指定商品
        hashOps.delete(skuId.toString());
    }
}
