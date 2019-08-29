package com.leyou.item.mapper;

import com.leyou.pojo.item.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.item
 * @version: 1.0
 */
public interface CategoryMapper  extends Mapper<Category>, SelectByIdListMapper<Category, Long> {
}
