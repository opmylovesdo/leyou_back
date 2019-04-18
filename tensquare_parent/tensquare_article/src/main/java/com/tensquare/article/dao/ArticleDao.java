package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * update tb_article a set a.thumbup=a.thumbup+1 where a.id = ?
     * update tb_article a set a.state = 1 where a.id = ?
     * @param articleId
     */
    @Modifying
    @Query(value="update tb_article a set a.state = 1 where a.id = ?1", nativeQuery = true)
    void updateState(String articleId);

    @Modifying
    @Query(value="update tb_article a set a.thumbup=a.thumbup+1 where a.id = ?", nativeQuery = true)
    void thumbup(String articleId);
}
