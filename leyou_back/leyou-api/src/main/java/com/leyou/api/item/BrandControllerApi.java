package com.leyou.api.item;

import com.leyou.common.pojo.response.PageResult;
import com.leyou.pojo.item.Brand;
import com.leyou.pojo.item.request.BrandListRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.api.item
 * @version: 1.0
 */
@Api(value = "品牌管理接口", description = "提供品牌的的增、删、改、查")
@RequestMapping("brand")
public interface BrandControllerApi {

    /**
     * 根据查询条件分页并排序查询品牌信息
     * @param brandListRequest 封装查询条件的对象
     * @return
     */
    @ApiOperation("分页查询品牌列表")
    @GetMapping("page")
    ResponseEntity<PageResult<Brand>> queryBrandByPage(BrandListRequest brandListRequest);

    /**
     * 保存品牌
     * @param brand
     * @param cids
     * @return
     */
    @ApiOperation("保存品牌")
    @PostMapping
    ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids);

    /**
     * 保存品牌2
     * @param request
     * @return
     */
//    @ApiOperation("保存品牌")
//    ResponseEntity<Void> saveBrand(BrandSaveRequest request);

    /**
     * 根据cid查询品牌列表
     * @param cid
     * @return
     */
    @ApiOperation("根据cid查询品牌列表")
    @GetMapping("cid/{cid}")
    ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid);

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @ApiOperation("根据id查询品牌")
    @GetMapping("{id}")
    ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id);
}
