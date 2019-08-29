package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.pojo.item.*;
import com.leyou.pojo.search.Goods;
import com.leyou.pojo.search.request.SearchRequest;
import com.leyou.pojo.search.response.SearchResult;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.dao.GoodsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: LUOLUO
 * @Date: 2019/8/23
 * @Description: com.leyou.search.service
 * @version: 1.0
 */
@Service
public class SearchService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 根据spu对象 构建Goods对象
     *
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException {
        //创建goods对象
        Goods goods = new Goods();


        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId()).getBody();

        //查询分类名称
        List<String> categoryNames = this.categoryClient.queryCategoryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3())).getBody();
        //all = title category brand
        //拼接all字段
        String all = String.format("%s %s %s", spu.getTitle(), StringUtils.join(categoryNames, " "), brand.getName());

        List<Long> prices = new ArrayList<>();
        List<Sku> skus = goodsClient.querySkuListBySpuId(spu.getId()).getBody();

        //只需要sku中的id title price image 等字段 所以使用Map保存sku
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());

            Map<String, Object> skuParamMap = new HashMap<>();
            skuParamMap.put("id", sku.getId());
            skuParamMap.put("title", sku.getTitle());
            skuParamMap.put("price", sku.getPrice());
            skuParamMap.put("image", StringUtils.isNotEmpty(sku.getImages()) ? StringUtils.split(sku.getImages(), ",") : "");
            skuMapList.add(skuParamMap);
        });

        //查询规格参数
        List<SpecParam> specParams = this.specificationClient.querySpecParams(null, spu.getCid3(), null, true).getBody();

        //根据spuId查询SpuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId()).getBody();
        //把通用规格参数反序列化
        Map<String, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {
        });
        //把特殊规格参数反序列化
        Map<String, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {
        });

        Map<String, Object> specsMap = new HashMap<>();
        //Map<String, Object> 先根据spu的cid查询所有的规格参数,再根据是否通用(generic) 在generic_spec/special_spec 中查询数据
        //generic_spec(paramId:value) {"1":"其它","2":"G9青春版（全网通版）","3":2016.0,"5":143,"6":"其它","7":"Android","8":"骁龙（Snapdragon)","9":"骁龙617（msm8952）","10":"八核","11":1.5,"14":5.2,"15":"1920*1080(FHD)","16":800.0,"17":1300.0,"18":3000.0}
        //最终=> paramValue:value(如: 处理器:骁龙8xx)(如果是numeric=true, value为所属的范围值(如100-200))
        specParams.stream().forEach(specParam -> {
            //判断是否是通用的param类型
            if (specParam.getGeneric()) {
                String value = genericSpecMap.get(specParam.getId().toString()).toString();
                //判断param是否是数字 是返回一个区间
                if (specParam.getNumeric()) {
                    value = chooseSegment(value, specParam.getSegments(), specParam.getUnit());
                }
                specsMap.put(specParam.getName(), value);
            } else {
                //special_spec {"4":["白色","金色","玫瑰金"],"12":["3GB"],"13":["16GB"]}
                //如 颜色:["白色","金色","玫瑰金"]/运行内存:["3GB"]/存储内存:["16GB"]
                List<Object> values = specialSpecMap.get(specParam.getId().toString());
                specsMap.put(specParam.getName(), values);
            }
        });

        goods.setId(spu.getId());
        goods.setAll(all);
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        //设置sku列表
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        //skus所有价格的集合
        goods.setPrice(prices);
        //设置规格参数
        goods.setSpecs(specsMap);

        return goods;
    }


    /**
     * 根据值 判断所属的区间
     *
     * @param value
     * @param segmentsStr 0-1.0,1.0-1.5,1.5-2.0,2.0-2.5,2.5-
     * @return
     */
    private String chooseSegment(String value, String segmentsStr, String unit) {
        double val = NumberUtils.toDouble(value);
        String[] segments = segmentsStr.split(",");
        String result = "其他";

        for (String segment : segments) {
            String[] segs = segment.split("-");
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }

            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + unit + "以上";
                } else if (begin == 0) {
                    result = segs[1] + unit + "以下";
                } else {
                    result = segment + unit;
                }
                break;
            }
        }

        return result;
    }

    /**
     * 条件查询商品
     *
     * @param request
     * @return
     */
    public SearchResult search(SearchRequest request) {

        if (StringUtils.isBlank(request.getKey())) {
            return null;
        }

        //构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 1、对key进行全文检索查询
//        QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
        QueryBuilder basicQuery = buildBoolQueryBuilder(request);
        queryBuilder.withQuery(basicQuery);
        // 2、通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

        // 3、分页
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        //进行分类和品牌的聚合
        String categoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        // 4、查询，获取结果
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        //解析聚合结果集
        List<Map<String, Object>> categories = getCategoryAggResult(goodsPage.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(goodsPage.getAggregation(brandAggName));

        //判断分类聚合的结果集大小, 为1则聚合
        List<Map<String, Object>> specs = null;
        if(categories.size() == 1){
            specs = getParamsAggResult((Long) categories.get(0).get("id"), basicQuery);
        }
        // 封装成需要的返回结果集
        return new SearchResult(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent(), categories, brands, specs);
    }

    /**
     * 构建bool查询构建器
     * @param request
     * @return
     */
    private QueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //添加查询条件
        boolQueryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));

        //添加过滤条件
        if(CollectionUtils.isEmpty(request.getFilter())){
            return boolQueryBuilder;
        }

        request.getFilter().entrySet().forEach(entry -> {
            String key = entry.getKey();
            //如果过滤条件是品牌
            if(StringUtils.equals("品牌", key)){
                key = "brandId";
            }else if(StringUtils.equals("分类", key)){
                key = "cid3";
            }else{
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        });

        return boolQueryBuilder;
    }

    private List<Map<String, Object>> getParamsAggResult(Long id, QueryBuilder basicQuery) {
        //创建自定义查询器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //基于基本的条件查询, 聚合规格参数
        queryBuilder.withQuery(basicQuery);
        //查询要聚合的规格参数
        List<SpecParam> params = this.specificationClient.querySpecParams(null, id, null, true).getBody();

        //添加聚合
        params.forEach(param -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs."+param.getName()+".keyword"));
        });

        //过滤结果集
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));

        //执行聚合查询
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        /*
        [
            {
                "k":"规格参数名",
                "options":["规格参数值","规格参数值"]
            }
        ]
         */
        // 定义一个集合，收集聚合结果集
        List<Map<String, Object>> paramMapList = new ArrayList<>();
        //解析聚合查询的结果集
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        aggregationMap.entrySet().forEach(entry -> {
            Map<String, Object> map = new HashMap<>();
            // 放入规格参数名
            map.put("k", entry.getKey());
            // 解析每个聚合 收集规格参数值
            List<String> options = ((StringTerms) entry.getValue()).getBuckets().stream()
                    .map(StringTerms.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            map.put("options", options);
            paramMapList.add(map);
        });

        return paramMapList;
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;
        // 获取所有的品牌id桶
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        // 解析所有的id桶，查询品牌
        return buckets.stream().map(
                 bucket -> this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue()).getBody())
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;

        //获取桶的集合, 转化成List<Map<String, Object>>
        return terms.getBuckets().stream().map(bucket -> {
            //获取分类id
            long id = bucket.getKeyAsNumber().longValue();
            //根据分类id获取分类名称
            String name = this.categoryClient.queryCategoryNamesByIds(Arrays.asList(id)).getBody().get(0);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            return map;

        }).collect(Collectors.toList());
    }


    public void createIndex(Long id) throws IOException {

        Spu spu = this.goodsClient.querySpuById(id).getBody();
        // 构建商品
        Goods goods = this.buildGoods(spu);

        // 保存数据到索引库
        this.goodsRepository.save(goods);
    }

    public void deleteIndex(Long id) {
        this.goodsRepository.deleteById(id);
    }
}

