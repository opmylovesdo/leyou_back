package com.leyou.pojo.item.request;

import com.leyou.pojo.item.Brand;
import lombok.Data;

import java.util.List;

/**
 * 添加品牌的请求类
 * @Auther: LUOLUO
 * @Date: 2019/8/13
 * @Description: com.leyou.pojo.item.request
 * @version: 1.0
 */
@Data
public class BrandSaveRequest extends Brand {

    private List<Long> cids;
}
