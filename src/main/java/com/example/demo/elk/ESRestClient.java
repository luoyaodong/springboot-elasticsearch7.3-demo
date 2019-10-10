package com.example.demo.elk;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ESRestClient {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ESRestClient.class);

    @Autowired
    private ElkConfig elkConfig;

    private static ESRestClient restClientUtil;

    private static RestHighLevelClient client = null;

    @PostConstruct
    public void init() {
        restClientUtil = this;
        restClientUtil.elkConfig = this.elkConfig;
    }

    public RestHighLevelClient getClient(){
        if(client != null){
            return client;
        }else {
            synchronized(ESRestClient.class) {
                /* client = new RestHighLevelClient(clientBuilder);*/
                HttpHost[] httpHosts = new HttpHost[restClientUtil.elkConfig.getHosts().size()];
                for (int i = 0; i<httpHosts.length;i++){
                    Hosts host = restClientUtil.elkConfig.getHosts().get(i);
                    httpHosts[i] = new HttpHost(host.getIp(),Integer.parseInt(host.getPort()),host.getSchema());
                }
                RestClientBuilder clientBuilder = RestClient.builder(httpHosts);
                client = new RestHighLevelClient(clientBuilder);
                logger.info("RestClientUtil intfo create rest high level client successful!");
                return client;
            }
        }
    }
}