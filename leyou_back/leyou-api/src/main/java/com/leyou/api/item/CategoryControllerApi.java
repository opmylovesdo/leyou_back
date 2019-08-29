package com.leyou.api.item;

import com.leyou.pojo.item.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.api.item
 * @version: 1.0
 */
@Api(value = "分类管理接口", description = "分类管理接口，提供课程的增、删、改、查")
@RequestMapping("category")
public interface CategoryControllerApi {

    @ApiOperation("根据pid查询分类")
    @GetMapping("list")
    ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam(name = "pid", defaultValue = "0") Long pid);


    @ApiOperation("根据分类id查询分类名称")
    @GetMapping("names")
    ResponseEntity<List<String>> queryCategoryNamesByIds(@RequestParam("ids") List<Long> ids);
}
