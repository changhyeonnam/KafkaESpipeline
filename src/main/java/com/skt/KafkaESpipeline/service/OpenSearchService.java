package com.skt.KafkaESpipeline.service;


import com.skt.KafkaESpipeline.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.search.SearchHit;
import org.opensearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class OpenSearchService {


    private final DateUtils dateUtils = new DateUtils();

    @Autowired
    private RestHighLevelClient highLevelClient;


    public String getData(){
        String indexPrefix = "txt-";
        String dateIndex = indexPrefix + dateUtils.getDateNowString();
        SearchRequest searchRequest = new SearchRequest(dateIndex);
        SearchResponse searchResponse;

        try{
            searchResponse = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);

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
