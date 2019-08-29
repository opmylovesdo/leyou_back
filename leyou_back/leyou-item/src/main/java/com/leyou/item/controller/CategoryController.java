package com.leyou.item.controller;

import com.leyou.api.item.CategoryControllerApi;
import com.leyou.item.service.CategoryService;
import com.leyou.pojo.item.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.item.controller
 * @version: 1.0
 */
@RestController
@RequestMapping("category")
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据pid查询分类(根据父节点id查询子节点)
     * @param pid
     * @return
     */
    @Override
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid) {
        if(pid == null || pid < 0){
            //响应400: 参数不合法
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories =  this.categoryService.queryCategoryByPid(pid);
        if(CollectionUtils.isEmpty(categories)){
            //404: 资源未找到
            return ResponseEntity.notFound().build();
        }
        //200: 查询成功
        return ResponseEntity.ok(categories);
}

    /**
     * 根据分类id列表查询分类名称列表
     * @param ids
     * @return
     */
    @Override
    @GetMapping("names")
    public ResponseEntity<List<String>> queryCategoryNamesByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = this.categoryService.queryCategoryNamesByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }
}
