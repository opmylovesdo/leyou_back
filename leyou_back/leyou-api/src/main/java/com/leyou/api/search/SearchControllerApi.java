package com.leyou.api.search;

import com.leyou.pojo.search.request.SearchRequest;
import com.leyou.pojo.search.response.SearchResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/23
 * @Description: com.leyou.api.search
 * @version: 1.0
 */
@Api(value = "搜索服务接口")
public interface SearchControllerApi {

    @ApiOperation("根据条件搜索商品")
    @PostMapping("page")
    ResponseEntity<SearchResult> search(@RequestBody SearchRequest request);
}
