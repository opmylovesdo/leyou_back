package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.response.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.pojo.item.*;
import com.leyou.pojo.item.request.GoodsSaveRequest;
import com.leyou.pojo.item.request.SpuListRequest;
import com.leyou.pojo.item.response.SpuListResponse;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/18
 * @Description: com.leyou.item.service
 * @version: 1.0
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    public static final Logger LOGGER = LoggerFactory.getLogger(GoodsService.class);


    /**
     * 条件查询SPU列表
     *
     * @param request
     * @return
     */
    public PageResult<SpuListResponse> querySpuListByPage(SpuListRequest request) {
        int page = 1;
        int rows = 5;
        //spu id title category brand
        if (request != null) {
            //条件查询
            Example example = new Example(Spu.class);
            Example.Criteria criteria = example.createCriteria();

            Boolean saleable = request.getSaleable();
            //添加是否上架过滤条件
            if (saleable != null) {
                criteria.andEqualTo("saleable", saleable);
            }

            //添加关键字模糊查询
            String key = request.getKey();
            if (StringUtils.isNotEmpty(key)) {
                criteria.andLike("title", "%" + key + "%");
            }

            //添加分页
            Integer page1 = request.getPage();
            Integer rows1 = request.getRows();
            if (page1 == null || page1 < 1) {
                page1 = page;
            }

            if (rows1 == null || rows1 < 1) {
                rows1 = rows;
            }

            PageHelper.startPage(page1, rows1);

            //执行查询
            List<Spu> spus = this.spuMapper.selectByExample(example);
            PageInfo<Spu> pageInfo = new PageInfo<>(spus);

            //spu -> SpuListResponse
            List<SpuListResponse> spuListResponses = spus.stream().map(spu -> {

                SpuListResponse response = new SpuListResponse();
                BeanUtils.copyProperties(spu, response);

                //设置分类名称
                List<String> cnameList = categoryService.queryCategoryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
                String cname = StringUtils.join(cnameList, "/");
                response.setCname(cname);

                //设置品牌名称
                Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
                response.setBname(brand.getName());
                return response;
            }).collect(Collectors.toList());


            //返回 PageResult
            return new PageResult(pageInfo.getTotal(), spuListResponses);
        }
        return new PageResult<>();
    }

    /**
     * 添加商品
     *
     * @param request
     */
    @Transactional
    public void saveGoods(GoodsSaveRequest request) {
        //添加spu
        //设置默认字段
        request.setId(null);
        request.setSaleable(true);
        request.setValid(true);
        request.setCreateTime(new Date());
        request.setLastUpdateTime(request.getCreateTime());
        this.spuMapper.insertSelective(request);

        //添加spu_details
        SpuDetail spuDetail = request.getSpuDetail();
        spuDetail.setSpuId(request.getId());
        this.spuDetailMapper.insertSelective(spuDetail);
        //保存sku和库存
        saveSkuAndStock(request);
        //发送消息
        sendMessage(request.getId(), "insert");
    }

    private void saveSkuAndStock(GoodsSaveRequest request) {
        List<Sku> skus = request.getSkus();
        skus.forEach(sku -> {
            //添加sku
            sku.setSpuId(request.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //添加stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        if (spuId != null) {
            return this.spuDetailMapper.selectByPrimaryKey(spuId);
        }

        return null;
    }

    /**
     * 根据spuId查询sku列表
     *
     * @param spuId
     * @return
     */
    public List<Sku> querySkuListBySpuId(Long spuId) {

        if (spuId != null) {
            Sku record = new Sku();
            record.setSpuId(spuId);
            List<Sku> skus = this.skuMapper.select(record);
            skus.forEach(sku -> {
                Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
                sku.setStock(stock.getStock());
            });

            return skus;
        }

        return null;
    }

    /**
     * 更新商品
     *
     * @param request
     * @return
     */
    @Transactional
    public void updateGoods(GoodsSaveRequest request) {
        //根据spuId查询要删除的sku
        Sku record = new Sku();
        record.setSpuId(request.getId());
        List<Sku> skus = this.skuMapper.select(record);

        //删除sku对应的stock
        List<Long> skuIds = skus.stream().map(Sku::getId).collect(Collectors.toList());
        skuIds.forEach(this.stockMapper::deleteByPrimaryKey);
//        skus.forEach(sku -> {
//            this.stockMapper.deleteByPrimaryKey(sku.getId());
//        });


        //删除sku
//        skuIds.stream().forEach(this.skuMapper::deleteByPrimaryKey);
        this.skuMapper.delete(record);

        //新增sku和stock
        this.saveSkuAndStock(request);

        //更新spu和spuDetail
        request.setCreateTime(null);
        request.setLastUpdateTime(new Date());
        request.setValid(null);
        request.setSaleable(null);

        this.spuMapper.updateByPrimaryKeySelective(request);
        this.spuDetailMapper.updateByPrimaryKey(request.getSpuDetail());

        //发送消息
        sendMessage(request.getId(), "update");
    }

    /**
     * 根据id查询spu
     *
     * @param id
     * @return
     */
    public Spu querySpuById(Long id) {

        if (id == null) return null;

        return this.spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 这里没有指定交换机，因此默认发送到了配置中的：leyou.item.exchange
     * 这里要把所有异常都try起来，不能让消息的发送影响到正常的业务逻辑
     * @param id
     * @param type
     */
    private void sendMessage(Long id, String type){
        try {
            //发送消息
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (AmqpException e) {
            LOGGER.error("{}商品消息发送异常,商品id:{}", type, id, e);
            e.printStackTrace();
        }
    }

    /**
     * 根据skuId查询sku
     * @param id
     * @return
     */
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }
}


