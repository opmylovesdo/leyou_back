package com.leyou.item.service;

import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.pojo.item.SpecGroup;
import com.leyou.pojo.item.SpecParam;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.item.service
 * @version: 1.0
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据cid查询规格组列表
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecGroupsByCid(Long cid) {

        if (cid != null) {
            SpecGroup record = new SpecGroup();
            record.setCid(cid);
            List<SpecGroup> groups = specGroupMapper.select(record);
            return groups;
        }

        return null;
    }

    /**
     * 根据gid查询规格列表
     *
     * @param gid 规格组id
     * @param cid 分类id
     * @param generic 是否为通用属性
     * @param searching 是否为查询字段
     * @return
     */
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        //设置的属性为null时 条件不起作用
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);
    }


    /**
     * 根据分类id查询规格组列表及规格组下的参数
     * @param cid
     * @return
     */
    public List<SpecGroup> querySpecGroupsWithParamsByCid(Long cid) {
        if(cid != null){
            List<SpecGroup> specGroups = this.querySpecGroupsByCid(cid);
            specGroups.forEach(group -> {
                List<SpecParam> specParams = this.querySpecParams(group.getId(), null, null, null);
                group.setParams(specParams);
            });
            return specGroups;
        }
        return null;
    }
}
