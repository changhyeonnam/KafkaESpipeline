package com.skt.KafkaESpipeline.service;


import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import com.skt.KafkaESpipeline.dto.KafkaConsumerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Service
public class ConsumerPollService {

    @Value("${spring.kafka.bootstrap-servers}")
    private String KAFKA_ADDRESS;
    @Value("${spring.kafka.consumer.group-id}")
    private String gid;
    @Value("${spring.kafka.deserializer}")
    private String DESERIALIZER;

    private KafkaConsumer<String,String> consumer;

    @Autowired
    private Gson gson;

    @PostConstruct
    private void init(){
        Properties configs = new Properties();

        configs.put("bootstrap.servers",KAFKA_ADDRESS);
        configs.put("group.id", gid);
        configs.put("key.deserializer",DESERIALIZER);
        configs.put("value.deserializer", DESERIALIZER);

        consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList("telegraflogs"));
    }

    @PreDestroy
    private void destroy(){
        try {
            consumer.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }


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