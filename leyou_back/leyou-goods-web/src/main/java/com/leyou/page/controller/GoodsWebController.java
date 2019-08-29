package com.leyou.page.controller;

import com.leyou.api.goods_web.GoodsWebControllerApi;
import com.leyou.page.service.GoodsHtmlService;
import com.leyou.page.service.GoodsWebService;
import com.leyou.pojo.item.Spu;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.controller
 * @version: 1.0
 */
@Controller
@RequestMapping("item")
public class GoodsWebController implements GoodsWebControllerApi {

    @Autowired
    private GoodsWebService goodsWebService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 跳转到商品详情页
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id") Long id){
        //获取要加载的数据
        Map<String, Object> modelMap = this.goodsWebService.loadData(id);
        //放入模型
        model.addAllAttributes(modelMap);
        //页面静态化
        this.goodsHtmlService.asyncExcute(id);
        return "item";
    }
}
