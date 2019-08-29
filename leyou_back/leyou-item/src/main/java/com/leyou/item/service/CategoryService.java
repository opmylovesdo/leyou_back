package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.pojo.item.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.item.service
 * @version: 1.0
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 根据pid查询分类(根据父节点id查询子节点)
     * @param pid
     * @return
     */
    public List<Category> queryCategoryByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        return this.categoryMapper.select(record);
    }

    /**
     * 根据多个id查询对应的分类的名字列表
     * @param ids
     * @return
     */
    public List<String> queryCategoryNamesByIds(List<Long> ids){
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        return list.stream().map(Category::getName).collect(Collectors.toList());
    }
}
