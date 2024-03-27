package com.skt.KafkaESpipeline.service;

import com.google.gson.Gson;
import com.skt.KafkaESpipeline.dto.KafkaConsumerData;
import com.skt.KafkaESpipeline.util.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ConsumerListenService {

    @Autowired
    private Gson gson;

    @Value("${opensearch.address}")
    private String openSearchAddress;

    @Value(("${opensearch.port}"))
    private Integer openSearchPort;

    private DateUtils dateUtils;

    private RestHighLevelClient esClient;
    private final String indexPrefix = "test-";

    @PostConstruct
    void init(){
        esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(openSearchAddress, openSearchPort, "http")));
    }

    @PreDestroy
    void destroy(){
        try{
            esClient.close();
        }
        catch (Exception e){
            log.error("Error occurred during resource cleanup: {}", e.getMessage());
        }
    }

    public void sendKafkaToEs(KafkaConsumerData kafkaConsumerData){
        try {
            String dateIndex = indexPrefix + dateUtils.getDateNowString();
            IndexRequest request = new IndexRequest(dateIndex);
            request.source(gson.toJson(kafkaConsumerData), XContentType.JSON);
            IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
            log.debug("Indexed document Id: {}", response.getId());
        }
        catch(IOException e){
            log.error("Error occurred while indexing data to ElasticSearch: {}", e.getMessage());
        }
    }


    @KafkaListener(topics="telegraflogs", groupId="nam")
    public void Listen(ConsumerRecord<String,String> record){

        KafkaConsumerData kafkaConsumerData = gson.fromJson(record.value(), KafkaConsumerData.class);
        log.debug("Kafka consumed data:{}", gson.toJson(kafkaConsumerData));

        sendKafkaToEs(kafkaConsumerData);

    }


}
