package com.leyou.pojo.item.request;

import com.leyou.common.pojo.request.PageRequestData;
import lombok.Data;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.pojo.item.request
 * @version: 1.0
 */
@Data
public class SpuListRequest extends PageRequestData {

    //查询关键字
    private String key;
    //是否上架
    private Boolean saleable;
}
