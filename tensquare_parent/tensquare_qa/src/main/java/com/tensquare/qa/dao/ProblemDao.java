package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    @Query(value = "SELECT * FROM tb_pl pl left join tb_problem pro on pl.problemid = pro.id where pl.labelid = ? ORDER BY pro.replytime DESC", nativeQuery = true)
    Page<Problem> newList(String labelId, Pageable pageable);
    @Query(value = "SELECT * FROM tb_pl pl left join tb_problem pro on pl.problemid = pro.id where pl.labelid = ? ORDER BY pro.reply DESC", nativeQuery = true)
    Page<Problem> hotList(String labelId, Pageable pageable);
    @Query(value = "SELECT * FROM tb_pl pl left join tb_problem pro on pl.problemid = pro.id where pl.labelid = ? ORDER BY pro.createtime DESC", nativeQuery = true)
    Page<Problem> waitList(String labelId, Pageable pageable);
}
