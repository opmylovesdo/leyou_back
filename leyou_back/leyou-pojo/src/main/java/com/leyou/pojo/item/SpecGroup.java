package com.leyou.pojo.item;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "tb_spec_group")
public class SpecGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cid;

    private String name;

    @Transient
    // 该组下的所有规格参数集合
    private List<SpecParam> params;

}