package com.skt.KafkaESpipeline.service;

import com.google.gson.Gson;
import com.skt.KafkaESpipeline.dto.KafkaConsumerData;
import com.skt.KafkaESpipeline.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.OpenSearchException;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConsumerListenService {

    @Autowired
    private Gson gson;



    private final DateUtils dateUtils = new DateUtils();

    @Autowired
    private RestHighLevelClient highLevelClient;
//    private RestClient lowLevelClient = highLevelClient.getLowLevelClient();


    public void sendKafkaToEs(KafkaConsumerData kafkaConsumerData){
        try {

            String indexPrefix = "txt-";
            String indexName = indexPrefix + dateUtils.getDateNowString();
            String message = gson.toJson(kafkaConsumerData);

            IndexRequest request = new IndexRequest(indexName).id(String.valueOf(UUID.randomUUID()))
                    .source(message, XContentType.JSON);
            IndexResponse response = highLevelClient.index(request, RequestOptions.DEFAULT);
            log.debug("Indexed document Id: {}", response.getId());
        }
        catch (OpenSearchException e){
            System.out.println("request = "+kafkaConsumerData.toString());
            log.error("ElasticsearchException:{}", e.getMessage());
        }
        catch(IOException e){
            log.error("Error occurred while indexing data to ElasticSearch: {}", e.getMessage());
        }
    }


    @KafkaListener(id = "batch-listener", topics="telegraflogs", groupId="nam")
    public void consumer1(List<Object>records){
        try {
            for(Object record : records) {
                KafkaConsumerData kafkaConsumerData = gson.fromJson(record.toString(), KafkaConsumerData.class);
                log.debug("Kafka consumed data:{}", gson.toJson(kafkaConsumerData));

                sendKafkaToEs(kafkaConsumerData);
            }
        }
        catch (Exception e){
            log.debug("--> {}" ,records.toString());
            log.error(e.getMessage());
        }
    }


}