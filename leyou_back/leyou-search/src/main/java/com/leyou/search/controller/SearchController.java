package com.leyou.search.controller;

import com.leyou.api.search.SearchControllerApi;
import com.leyou.common.pojo.response.PageResult;
import com.leyou.pojo.search.Goods;
import com.leyou.pojo.search.request.SearchRequest;
import com.leyou.pojo.search.response.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/23
 * @Description: com.leyou.search.controller
 * @version: 1.0
 */
@RestController
public class SearchController implements SearchControllerApi {

    @Autowired
    private SearchService searchService;

    /**
     * 条件查询商品
     * @param request
     * @return
     */
    @Override
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest request){
        SearchResult result = this.searchService.search(request);
        if(result == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
