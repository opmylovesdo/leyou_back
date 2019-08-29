package com.leyou.item.controller;

import com.leyou.api.item.GoodsControllerApi;
import com.leyou.common.pojo.response.PageResult;
import com.leyou.item.service.GoodsService;
import com.leyou.pojo.item.Sku;
import com.leyou.pojo.item.Spu;
import com.leyou.pojo.item.SpuDetail;
import com.leyou.pojo.item.request.GoodsSaveRequest;
import com.leyou.pojo.item.request.SpuListRequest;
import com.leyou.pojo.item.response.SpuListResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.item.controller
 * @version: 1.0
 */
@RestController
public class GoodsController implements GoodsControllerApi {

    @Autowired
    private GoodsService goodsService;


    /**
     * 条件查询SPU列表
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping(value = "spu/page")
    @Override
    public ResponseEntity<PageResult<SpuListResponse>> querySpuListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        SpuListRequest request = new SpuListRequest();
        request.setKey(key);
        request.setSaleable(saleable);
        request.setPage(page);
        request.setRows(rows);

        PageResult<SpuListResponse> pageResult = this.goodsService.querySpuListByPage(request);

        if (CollectionUtils.isEmpty(pageResult.getItems())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 添加商品
     *
     * @param request
     * @return
     */
    @PostMapping("goods")
    @Override
    public ResponseEntity<Void> saveGoods(@RequestBody GoodsSaveRequest request) {
        this.goodsService.saveGoods(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 更新商品
     *
     * @param request
     * @return
     */
    @PutMapping("goods")
    @Override
    public ResponseEntity<Void> updateGoods(@RequestBody GoodsSaveRequest request) {
        this.goodsService.updateGoods(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    @Override
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (null == spuDetail) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spuId查询sku列表
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    @Override
    public ResponseEntity<List<Sku>> querySkuListBySpuId(@RequestParam("id") Long spuId) {
        List<Sku> skus = this.goodsService.querySkuListBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id) {
        Spu spu = this.goodsService.querySpuById(id);
        if (null == spu) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }


    /**
     * 根据sku的id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if(null == sku){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }
}
