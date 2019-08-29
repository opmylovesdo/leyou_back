package com.leyou.api.goods_web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.api.goods_web
 * @version: 1.0
 */
@Api("页面详情接口")
@RequestMapping("item")
public interface GoodsWebControllerApi {

    /**
     * 跳转到商品详情页
     * @param model
     * @return
     */
    @GetMapping("{id}.html")
    @ApiOperation("跳转到商品详情页")
    String toItemPage(Model model, @PathVariable("id") Long id);

}
