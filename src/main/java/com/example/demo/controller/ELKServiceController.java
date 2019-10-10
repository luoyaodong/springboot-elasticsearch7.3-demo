package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.services.ElasticSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/elk")
public class ELKServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ELKServiceController.class);

    @Autowired
    ElasticSearchService searchService;

    @RequestMapping("/service/{filedValue}")
    @ResponseBody
    public JSONObject matchService(@PathVariable String filedValue){
        logger.info("进入matchService方法");
        JSONObject resJSON = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        BoolQueryBuilder builder = searchService.boolMatchQuery("name",filedValue);
        SearchResponse searchResponse = searchService.searchMessage(builder);
        SearchHits searchHits = null;
        searchHits =  searchResponse.getHits();//获取响应中的列表数据
        int total = searchHits.getHits().length;
        for(SearchHit hit:searchHits.getHits()){// 遍历构造返回JSON，以下不再多说
            JSONObject dataJSON = new JSONObject();
            String tempRes = hit.getSourceAsString();
            dataJSON = JSONObject.parseObject(tempRes);
            jsonArr.add(dataJSON);
        }
        resJSON.put("resArr", jsonArr);
        resJSON.put("total", total);
        logger.info("离开matchService方法，返回resJSON");
        return resJSON;
    }
}
