package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Auther: LUOLUO
 * @Date: 2019/4/16
 * @Description: com.tensquare.base.dao
 * @version: 1.0
 */
public interface LabelDao extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {


}
