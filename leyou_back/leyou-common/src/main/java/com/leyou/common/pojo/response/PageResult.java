package com.leyou.common.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.common.pojo.response
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    //总条数
    private Long total;
    //总页数
    private Integer totalPage;
    //当前页数据
    private List<T> items;

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }
}
