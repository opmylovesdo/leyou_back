package com.leyou.page.listener;

import com.leyou.page.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.page.listener
 * @version: 1.0
 */
@Component
public class GoodsWebListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.create.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"item.insert", "item.update"}
    ))
    public void listenCreate(Long id) {
        if(id == null){
            return ;
        }
        //创建页面
        goodsHtmlService.createHtml(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "leyou.delete.web.queue", durable = "true"),
            exchange = @Exchange(
                    value = "leyou.item.exchange", type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"item.delete"}
    ))
    public void listenDelete(Long id) {
        if(id == null){
            return ;
        }
        //删除页面
        goodsHtmlService.deleteHtml(id);
    }
}
