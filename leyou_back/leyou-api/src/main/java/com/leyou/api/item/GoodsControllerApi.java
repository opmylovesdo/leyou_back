package com.leyou.api.item;

import com.leyou.common.pojo.response.PageResult;
import com.leyou.pojo.item.Sku;
import com.leyou.pojo.item.Spu;
import com.leyou.pojo.item.SpuDetail;
import com.leyou.pojo.item.request.GoodsSaveRequest;
import com.leyou.pojo.item.response.SpuListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.api.item
 * @version: 1.0
 */
@Api("商品管理接口")
public interface GoodsControllerApi {

    /**
     * 条件查询SPU列表
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @ApiOperation("条件查询SPU列表")
    @GetMapping("spu/page")
    ResponseEntity<PageResult<SpuListResponse>> querySpuListByPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                                   @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
                                                                   @RequestParam(value = "key", required = false) String key);

    /**
     * 添加商品
     *
     * @param request
     * @return
     */
    @ApiOperation("保存商品")
    @PostMapping("goods")
    ResponseEntity<Void> saveGoods(GoodsSaveRequest request);

    /**
     * 更新商品
     *
     * @param request
     * @return
     */
    @ApiOperation("更新商品")
    @PutMapping("goods")
    ResponseEntity<Void> updateGoods(GoodsSaveRequest request);

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    @ApiOperation("根据spuId查询SpuDetail")
    @GetMapping("spu/detail/{spuId}")
    ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId查询sku列表
     *
     * @param spuId
     * @return
     */
    @ApiOperation("根据spuId查询sku列表")
    @GetMapping("sku/list")
    ResponseEntity<List<Sku>> querySkuListBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @ApiOperation("根据spu的id查询spu")
    @GetMapping("spu/{id}")
    ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id);


    /**
     * 根据sku的id查询sku
     * @param id
     * @return
     */
    @ApiOperation("根据sku的id查询sku")
    @GetMapping("sku/{id}")
    ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id);
}
