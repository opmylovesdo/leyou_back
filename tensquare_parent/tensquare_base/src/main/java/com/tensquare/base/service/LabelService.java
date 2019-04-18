package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: LUOLUO
 * @Date: 2019/4/16
 * @Description: com.tensquare.base.service
 * @version: 1.0
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     *      * 查询全部标签
     *      * @return
     *      
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     *      * 根据ID查询标签
     *      * @return
     *      
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     *      * 增加标签
     *      * @param label
     *      
     */
    public void add(Label label) {
        label.setId(idWorker.nextId() + "");//设置id
        labelDao.save(label);
    }

    /**
     *      * 修改标签
     *      * @param label
     *      
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     *      * 删除标签
     *      * @param id
     *      
     */
    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    /**
     * 条件查询所有
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label) {
        return labelDao.findAll(createSpecification(label));
    }

    /**
     * 条件分页查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Label label, int page, int size){
        Specification<Label> specification = createSpecification(label);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification, pageRequest);
    }

    private Specification<Label> createSpecification(Label label) {
        return new Specification<Label>() {
            /**
             *
             * @param root 根对象, 封装了条件的对象 where 列名=label.getid
             * @param query 封装查询的关键字 group by order by 等等
             * @param criteriaBuilder 用来封装条件对象的, return null 表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotEmpty(label.getLabelname())) {
                    Predicate predicate = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }
                if (StringUtils.isNotEmpty(label.getState())) {
                    Predicate predicate = criteriaBuilder.equal(root.get("state").as(String.class), label.getState());
                    list.add(predicate);
                }
                if (StringUtils.isNotEmpty(label.getRecommend())) {
                    Predicate predicate = criteriaBuilder.equal(root.get("recommend").as(String.class), label.getRecommend());
                    list.add(predicate);
                }

                Predicate[] parr = new Predicate[list.size()];
                list.toArray(parr);
                return criteriaBuilder.and(parr);
            }
        };
    }



}
