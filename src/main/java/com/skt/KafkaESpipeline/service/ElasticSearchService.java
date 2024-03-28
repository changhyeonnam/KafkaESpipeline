package com.skt.KafkaESpipeline.service;


import com.skt.KafkaESpipeline.util.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ElasticSearchService {


    private final DateUtils dateUtils = new DateUtils();

    private RestHighLevelClient client;

    @Value("${opensearch.address}")
    private String openSearchAddress;

    @Value(("${opensearch.port}"))
    private Integer openSearchPort;
    @PostConstruct
    void init(){
        client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(openSearchAddress, openSearchPort, "http")));
    }

    @PreDestroy
    void destroy(){
        try{
            client.close();
        }
        catch (Exception e){
            log.error("Error occurred during resource cleanup: {}", e.getMessage());
        }
    }


    public String getEsData(){
        String indexPrefix = "text-";
        String dateIndex = indexPrefix + dateUtils.getDateNowString();
        SearchRequest searchRequest = new SearchRequest(dateIndex);

        SearchResponse searchResponse;

        try{
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            String result = "";
            for(SearchHit hit : hits.getHits()){
                result += hit.getSourceAsString()+'\n';
            }

            return result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
