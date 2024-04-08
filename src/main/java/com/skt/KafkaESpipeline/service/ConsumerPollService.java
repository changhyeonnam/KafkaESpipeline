package com.skt.KafkaESpipeline.service;


import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import com.skt.KafkaESpipeline.dto.KafkaConsumerData;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Map;

@Slf4j
@Service
public class ConsumerPollService {


    private final String KAFKA_ADDRESS;
    private final String groupId;
    private final String DESERIALIZER;
    private KafkaConsumer<String,String> consumer;
    private final Gson gson;

    @Autowired
    public ConsumerPollService(@Value("${spring.kafka.bootstrap-servers}") String KAFKA_ADDRESS,
                               @Value("${spring.kafka.consumer.group-id}")String groupId,
                               @Value("${spring.kafka.deserializer}")String DESERIALIZER,
                               Gson gson){

        this.KAFKA_ADDRESS = KAFKA_ADDRESS;
        this.groupId = groupId;
        this.DESERIALIZER = DESERIALIZER;
        this.gson = gson;
    }


    /**
     *
     */
    @PostConstruct
    private void init(){
        Properties configs = new Properties();
        configs.put("bootstrap.servers",this.KAFKA_ADDRESS);
        configs.put("group.id", this.groupId);
        configs.put("key.deserializer",this.DESERIALIZER);
        configs.put("value.deserializer", this.DESERIALIZER);
        consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList("telegraflogs"));
    }

    /**
     *
     */
    @PreDestroy
    private void destroy(){
        try {
            consumer.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public String consumepoll(){
        String result = null;

        while(true){
            log.debug("poll record");

            Set<String> topics = consumer.subscription();
            log.debug("Subscribed topics: " + topics);

            ConsumerRecords<String,String> records = consumer.poll(500);

            if(!records.isEmpty()) {
                log.debug("records is not empty.");
                for (ConsumerRecord<String, String> record : records) {

                    // json -> object
                    KafkaConsumerData kafkaConsumerData = gson.fromJson(record.value(), KafkaConsumerData.class);
                    log.debug("kafka consume data; {}",gson.toJson(kafkaConsumerData));

                    // object -> json
                    result = gson.toJson(kafkaConsumerData);
                    return result;
                }
            }
        }




    }



}