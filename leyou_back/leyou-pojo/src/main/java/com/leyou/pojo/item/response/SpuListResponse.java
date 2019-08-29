package com.leyou.pojo.item.response;

import com.leyou.pojo.item.Spu;
import lombok.Data;

/**
 * 查询Spu列表时的返回值
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.pojo.item.response
 * @version: 1.0
 */
@Data
public class SpuListResponse extends Spu {

    private String cname;
    private String bname;
}
