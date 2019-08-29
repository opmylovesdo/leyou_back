package com.leyou.common.utils.goods_web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/25
 * @Description: com.leyou.common
 * @version: 1.0
 */
public class ThreadUtils {

    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        es.submit(runnable);
    }
}