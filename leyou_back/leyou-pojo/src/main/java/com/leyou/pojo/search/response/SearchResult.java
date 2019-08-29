package com.leyou.pojo.search.response;

import com.leyou.common.pojo.response.PageResult;
import com.leyou.pojo.item.Brand;
import com.leyou.pojo.search.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/23
 * @Description: com.leyou.pojo.search.response
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult extends PageResult<Goods> {

    //只需要分类id和name 所以使用Map保存属性(节省带宽)
    private List<Map<String, Object>> categories;
    private List<Brand> brands;
    private List<Map<String, Object>> specs;



    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
