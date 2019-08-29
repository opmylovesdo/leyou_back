package com.leyou.common.pojo.request;

import lombok.Data;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.common.pojo.request
 * @version: 1.0
 */
@Data
public class PageRequestData extends RequestData {
    //目标页数
    private Integer page;
    //每页显示函数
    private Integer rows;
}
