package com.leyou.item.controller;

import com.leyou.api.item.SpecControllerApi;
import com.leyou.item.service.SpecificationService;
import com.leyou.pojo.item.SpecGroup;
import com.leyou.pojo.item.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("spec")
public class SpecificationController implements SpecControllerApi {


    @Autowired
    private SpecificationService specificationService;


    /**
     * 根据cid查询规格组列表
     * @param cid
     * @return
     */
    @Override
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsByCid(@PathVariable("cid") Long cid) {

        List<SpecGroup> groups = this.specificationService.querySpecGroupsByCid(cid);
        if(CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }


    /**
     * 根据gid查询规格列表
     * @param gid
     * @return
     */
    @GetMapping("params")
    @Override
    public ResponseEntity<List<SpecParam>> querySpecParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching
    ) {
        List<SpecParam> params = this.specificationService.querySpecParams(gid, cid, generic, searching);

        if(CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }


    /**
     * 根据cid查询规格组列表和组内参数列表
     * @param cid
     * @return
     */
    @GetMapping("groups/param/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupsWithParamsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> specGroups = this.specificationService.querySpecGroupsWithParamsByCid(cid);
        if(CollectionUtils.isEmpty(specGroups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specGroups);
    }



}
