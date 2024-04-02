package com.skt.KafkaESpipeline.service;


import com.skt.KafkaESpipeline.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenSearchService {



    private final RestHighLevelClient restHighLevelClient;
    private final DateUtils dateUtils;

    @Autowired
    public OpenSearchService(RestHighLevelClient restHighLevelClient, DateUtils dateUtils){
        this.restHighLevelClient = restHighLevelClient;
        this.dateUtils = dateUtils;
    }

    public List<Map<String,Object>> getOpenSearchData(){
        String dateIndex = dateUtils.getIndex();
        SearchRequest searchRequest = new SearchRequest(dateIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(30);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;

        try{
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();

            List<Map<String,Object>> results = new ArrayList<>();
            for(SearchHit hit : hits.getHits()){
                Map<String,Object> map = new HashMap<>();
                hit.getSourceAsMap().forEach((key,value)-> map.put(key,value));
                results.add(map);
            }

            return results;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
