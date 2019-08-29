package com.leyou.search.dao;

import com.leyou.pojo.search.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/22
 * @Description: com.leyou.search.dao
 * @version: 1.0
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
