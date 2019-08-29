package com.leyou.pojo.search.request;

import lombok.Data;

import java.util.Map;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/23
 * @Description: com.leyou.pojo.search
 * @version: 1.0
 */
@Data
public class SearchRequest {
    private String key;// 搜索条件
    private Integer page;// 当前页
    //过滤条件
    private Map<String, String> filter;

    private static final Integer DEFAULT_SIZE = 20;// 每页大小，不从页面接收，而是固定大小
    private static final Integer DEFAULT_PAGE = 1;// 默认页




    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }


    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}