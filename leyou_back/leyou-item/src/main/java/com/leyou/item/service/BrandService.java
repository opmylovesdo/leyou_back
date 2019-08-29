package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.response.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.pojo.item.Brand;
import com.leyou.pojo.item.request.BrandListRequest;
import com.leyou.pojo.item.request.BrandSaveRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/11
 * @Description: com.leyou.item.service
 * @version: 1.0
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件分页并排序查询品牌信息
     * @param brandListRequest 封装查询条件的对象
     * @return
     */
    public PageResult<Brand> queryBrandByPage(BrandListRequest brandListRequest) {
        int page = 1;
        int rows = 5;
        if(brandListRequest != null){

            Integer page1 = brandListRequest.getPage();
            if(page1 != null && page1 > 0){
                page = page1;
            }

            Integer rows1 = brandListRequest.getRows();
            if(rows1 != null && rows1 > 0){
                rows = rows1;
            }

            //初始化example对象
            Example example = new Example(Brand.class);
            Example.Criteria criteria = example.createCriteria();

            //获取查询关键字
            String key = brandListRequest.getKey();
            if(StringUtils.isNotEmpty(key)){
                //根据name模糊查询, 或者首字母查询
                criteria.andLike("name", "%" +key + "%").orEqualTo("letter", key);
            }

            //添加分页
            PageHelper.startPage(page, rows);

            //添加排序条件
            //获取排序字段
            String sortBy = brandListRequest.getSortBy();
            if(StringUtils.isNotEmpty(sortBy)){
                //判断是否降序
                Boolean desc = brandListRequest.getDesc();
                String sort = "desc";
                if(desc == null || !desc){
                    sort = "asc";
                }
                example.setOrderByClause(sortBy + " " + sort);
            }


            List<Brand> brands = this.brandMapper.selectByExample(example);

            //包装成PageInfo对象
            PageInfo<Brand> pageInfo = new PageInfo<>(brands);

            //包装成分页结果集返回
            return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
        }
        //返回一个空的结果集
        return new PageResult<>();
    }

    /**
     * 保存品牌
     * @param brand
     * @param cids
     * @return
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //保存品牌
        this.brandMapper.insertSelective(brand);

        //保存品牌-分类中间关系
        cids.forEach(cid -> {
            this.brandMapper.insertBrandAndCategory(cid, brand.getId());
        });
    }

    /**
     * 保存品牌2
     * @param request
     * @return
     */
    @Transactional
    public void saveBrand(BrandSaveRequest request) {
        //保存品牌
        this.brandMapper.insertSelective(request);

        //保存品牌-分类中间关系
        List<Long> cids = request.getCids();
        cids.forEach(cid -> {
            this.brandMapper.insertBrandAndCategory(cid, request.getId());
        });
    }


    /**
     * 根据cid查询品牌列表
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {

        if(cid != null){
            return this.brandMapper.selectBrandByCid(cid);
        }

        return null;
    }

    /**
     * 根据id查询品牌对象
     * @param id
     * @return
     */
    public Brand queryBrandById(Long id) {

        if(null != id){
            return this.brandMapper.selectByPrimaryKey(id);
        }

        return null;
    }
}
