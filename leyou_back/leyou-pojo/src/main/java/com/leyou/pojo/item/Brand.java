package com.leyou.pojo.item;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.pojo.item
 * @version: 1.0
 */
@Data
@Table(name = "tb_brand")
public class Brand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //品牌名称
    private String name;
    //品牌图片
    private String image;
    //品牌首字母
    private Character letter;


}
