package com.example.demo.services;

import com.example.demo.elk.ESRestClient;
import com.example.demo.elk.ElkConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    @Autowired
    private ESRestClient esRestClient;

    @Autowired
    private ElkConfig elkConfig;

    /**
     * 根据布尔条件进行查询
     * @param boolQueryBuilder
     * @return
     */
    public SearchResponse searchMessage(BoolQueryBuilder boolQueryBuilder) {
        try {
            RestHighLevelClient restClient = esRestClient.getClient();
            SearchRequest searchRequest = new SearchRequest(elkConfig.getIndex());

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(100);
            sourceBuilder.query(boolQueryBuilder);
            logger.info(sourceBuilder.toString());
            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = restClient.search(searchRequest, RequestOptions.DEFAULT);
            searchResponse.getHits().forEach(message -> {
                try {
                    String sourceAsString = message.getSourceAsString();
                    logger.info(sourceAsString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return searchResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't get Detail");
        }
    }
    /***
      * @description 增加一条demo数据
      * @params [source]
      * @return void
      * @author luoyaodong
      * @date 2019/10/10 19:10
      */
    public void addRequest(String source) {

        RestHighLevelClient restClient = esRestClient.getClient();
        IndexRequest indexRequest = new IndexRequest(elkConfig.getIndex());
        indexRequest.id("5");
        indexRequest.source(source, XContentType.JSON);
        try {
            restClient.index(indexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't get Detail");
        }
        return;
    }

    /**
     * 单条件检索
     * @param fieldKey
     * @param fieldValue
     * @return
     */
    public MatchPhraseQueryBuilder uniqueMatchQuery(String fieldKey, String fieldValue){
        MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery(fieldKey,fieldValue);
        return matchPhraseQueryBuilder;
    }

    /**
     * 多条件检索并集，适用于搜索比如包含腾讯大王卡，滴滴大王卡的用户
     * @param fieldKey
     * @param queryList
     * @return
     */
    public BoolQueryBuilder orMatchUnionWithList(String fieldKey, List<String> queryList){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (String fieldValue : queryList){
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery(fieldKey,fieldValue));
        }
        return boolQueryBuilder;
    }

    /***
      * @description bool单条件查询函数
      * @params [fieldKey, filedValue]
      * @return org.elasticsearch.index.query.BoolQueryBuilder
      * @author luoyaodong
      * @date 2019/10/10 16:17
      */
    public BoolQueryBuilder boolMatchQuery(String fieldKey, String filedValue) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery(fieldKey,filedValue));
        return boolQueryBuilder;
    }

    /**
     * 范围查询，左右都是闭集
     * @param fieldKey
     * @param start
     * @param end
     * @return
     */
    public RangeQueryBuilder rangeMathQuery(String fieldKey, String start, String end){
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(fieldKey);
        rangeQueryBuilder.gte(start);
        rangeQueryBuilder.lte(end);
        return rangeQueryBuilder;
    }

    /**
     * 根据中文分词进行查询
     * @param fieldKey
     * @param fieldValue
     * @return
     */
    public MatchQueryBuilder matchQueryBuilder(String fieldKey, String fieldValue){
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(fieldKey,fieldValue).analyzer("ik_smart");
        return matchQueryBuilder;
    }



}