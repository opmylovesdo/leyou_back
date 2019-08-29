package com.leyou.item.controller;

import com.leyou.api.item.BrandControllerApi;
import com.leyou.common.pojo.response.PageResult;
import com.leyou.item.service.BrandService;
import com.leyou.pojo.item.Brand;
import com.leyou.pojo.item.request.BrandListRequest;
import com.leyou.pojo.item.request.BrandSaveRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.item.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("brand")
public class BrandController implements BrandControllerApi {

    @Autowired
    private BrandService brandService;

    /**
     * 根据查询条件分页并排序查询品牌信息
     * @param brandListRequest 封装查询条件的对象
     * @return
     */
    @Override
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(BrandListRequest brandListRequest) {
        PageResult<Brand> pageResult = this.brandService.queryBrandByPage(brandListRequest);

        if(CollectionUtils.isEmpty(pageResult.getItems())){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 保存品牌
     * @param brand
     * @param cids
     * @return
     */
    @Override
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 保存品牌2 前端cids 不能传"1,2,3"字符串(在参数列表时可解析),无法解析
     * 传[1,2,3]
     * @param request
     * @return
     */
//    @Override
//    public ResponseEntity<Void> saveBrand(@RequestBody BrandSaveRequest request) {
//        this.brandService.saveBrand(request);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    /**
     * 根据cid查询品牌列表
     * @param cid
     * @return
     */
    @Override
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        List<Brand> brands = this.brandService.queryBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = this.brandService.queryBrandById(id);

        if(null == brand){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(brand);
    }
}
