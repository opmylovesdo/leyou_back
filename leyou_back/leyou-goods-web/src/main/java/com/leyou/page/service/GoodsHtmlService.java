package com.leyou.page.service;

import com.leyou.common.utils.goods_web.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.page.service
 * @version: 1.0
 */
@Service
public class GoodsHtmlService {

    @Autowired
    private GoodsWebService goodsWebService;

    @Autowired
    private TemplateEngine engine;

    public static final Logger LOGGER = LoggerFactory.getLogger(GoodsHtmlService.class);


    /**
     * 创建html页面
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) {
        if (spuId != null) {

            Map<String, Object> spuMap = this.goodsWebService.loadData(spuId);
            //创建thymeleaf上下文对象
            Context context = new Context();
            //把数据放入上下文对象
            context.setVariables(spuMap);

            //创建输出流对象
            File file = new File("D:\\leyou\\nginx-1.14.0\\html\\item\\" + spuId + ".html");

            //文件存在先删除
            file.deleteOnExit();

            try (PrintWriter writer = new PrintWriter(file)) {
                // 执行页面静态化方法
                engine.process("item", context, writer);
            } catch (FileNotFoundException e) {
                LOGGER.error("页面静态化出错：{}，" + e, spuId);
                e.printStackTrace();
            }


        }
    }


    /**
     * 新建线程处理页面静态化
     * @param spuId
     */
    public void asyncExcute(Long spuId) {
        ThreadUtils.execute(()->createHtml(spuId));
        /*ThreadUtils.execute(new Runnable() {
            @Override
            public void run() {
                createHtml(spuId);
            }
        });*/
    }

    /**
     * 删除静态页面
     * @param id
     */
    public void deleteHtml(Long id) {
        File file = new File("C:\\project\\nginx-1.14.0\\html\\item\\", id + ".html");
        file.deleteOnExit();
    }
}
