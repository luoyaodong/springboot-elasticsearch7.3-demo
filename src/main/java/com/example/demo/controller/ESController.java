package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/")
public class ESController {
    /**
     * 查询phoneList
     * @param
     * @return
     */
    @RequestMapping(value = "/selPhoneList", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String selPhoneList(String jmpt_name, String mobileLocation, String pageSize, String currentPage) {
        // 引入client，配置按各自修改
        RestHighLevelClient client = new RestHighLevelClient( RestClient.builder( new HttpHost("192.144.154.86", Integer.parseInt("9200"), "http")));

        jmpt_name="NFZF15045871807281445364228";
        mobileLocation="zhangxueyou";

        JSONObject resJSON = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        int currentPageInt = 0;
        int pageSizeInt = 2;
        // 提取分页参数
        if (jmpt_name == null || "undefined".equals(jmpt_name)) {
            jmpt_name = "";
        }
        if (mobileLocation == null || "undefined".equals(mobileLocation)) {
            mobileLocation = "";
        }
        if (pageSize != null && !"".equals(pageSize)) {
            pageSizeInt = Integer.parseInt(pageSize);
        }
        if (currentPage != null && !"".equals(currentPage)) {
            currentPageInt = Integer.parseInt(currentPage) * pageSizeInt;
        }

        // 查询流程***（重要）：子查询对象（QueryBuilder）-->父查询对象（BoolQueryBuilder）-->查询函数构造对象（SearchSourceBuilder）-->请求发起对象（SearchRequest ）-->发起请求-->返回结果（SearchResponse）
        // 创建父查询对象
        BoolQueryBuilder srBuilder = QueryBuilders.boolQuery();
        // 创建子查询对象
        QueryBuilder jmpt_nameBuilder = null;
        QueryBuilder locationBuilder = null;
        // 创建查询函数构造对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 参数注入到
        if (!"".equals(jmpt_name)){
            jmpt_nameBuilder = QueryBuilders.queryStringQuery(jmpt_name).field("orderId");
            srBuilder.must(jmpt_nameBuilder);//子查询对象放入父查询对象中
        }
        if (!"".equals(mobileLocation)){
            locationBuilder = QueryBuilders.queryStringQuery(mobileLocation).field("name");

            srBuilder.must(locationBuilder);//子查询对象放入父查询对象中
        }

        sourceBuilder.query(srBuilder); // 把父查询对象放入函数构造对象中
        sourceBuilder.from(currentPageInt); // 参数范围起
        sourceBuilder.size(pageSizeInt); // 参数范围始
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));// 设置超时时间
        sourceBuilder.trackTotalHits(false); // 取消默认最大查询数量上限（默认10000）

        System.out.println("打印提交的DSL语句：sourceBuilder--：" + sourceBuilder);
        // 构造 请求发起对象
        SearchRequest searchRequest = new SearchRequest("demo");// 这里直接配置索引名即可
        // searchRequest.indices("phone");
        searchRequest.source(sourceBuilder);// 把查询函数构造对象注入查询请求中
        SearchResponse searchResponse;// 创建响应对象

        SearchHits searchHits = null;
        try {
            searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
            searchHits =  searchResponse.getHits();//获取响应中的列表数据
//            long total = searchHits.getTotalHits().value;//获取响应中的列表数据总数 这个方法会报空指针异常，因为返回的数据里面没有total.value这个值
            int total = searchHits.getHits().length;

            for(SearchHit hit:searchHits.getHits()){// 遍历构造返回JSON，以下不再多说
                JSONObject dataJSON = new JSONObject();
                String tempRes = hit.getSourceAsString();
                dataJSON = JSONObject.parseObject(tempRes);
                jsonArr.add(dataJSON);
            }

            resJSON.put("resArr", jsonArr);
            resJSON.put("total", total);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resJSON.toJSONString();
    }
}
