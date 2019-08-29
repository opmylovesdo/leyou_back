package com.leyou.pojo.item.request;

import com.leyou.common.pojo.request.PageRequestData;
import lombok.Data;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.pojo.item.request
 * @version: 1.0
 */
@Data
public class BrandListRequest extends PageRequestData {
    //查询关键字
    private String key;
    //排序字段
    private String sortBy;
    //是否降序排序
    private Boolean desc;
}
