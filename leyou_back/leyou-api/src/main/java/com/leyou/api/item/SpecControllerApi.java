package com.leyou.api.item;

import com.leyou.pojo.item.SpecGroup;
import com.leyou.pojo.item.SpecParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.api.item
 * @version: 1.0
 */
@Api("规格参数/组管理接口")
@RequestMapping("spec")
public interface SpecControllerApi {

    /**
     * 根据gid查询规格列表
     * @param gid
     * @return
     */
    @GetMapping("params")
    @ApiOperation("根据gid/cid/generic/searching查询规格列表")
    ResponseEntity<List<SpecParam>> querySpecParams(@RequestParam(value = "gid", required = false) Long gid,
                                                         @RequestParam(value = "cid", required = false)Long cid,
                                                         @RequestParam(value = "generic", required = false)Boolean generic,
                                                         @RequestParam(value = "searching", required = false)Boolean searching);


    /**
     * 根据cid查询规格组列表
     * @param cid
     * @return
     */
    @ApiOperation("根据cid查询规格组列表")
    @GetMapping("groups/{cid}")
    ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable("cid") Long cid);

    /**
     * 根据cid查询规格组列表和组内参数列表
     * @param cid
     * @return
     */
    @ApiOperation("根据cid查询规格组列表和组内参数列表")
    @GetMapping("groups/param/{cid}")
    ResponseEntity<List<SpecGroup>> querySpecGroupsWithParamsByCid(@PathVariable("cid") Long cid);





}
