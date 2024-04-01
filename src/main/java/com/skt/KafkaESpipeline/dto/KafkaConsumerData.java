package com.skt.KafkaESpipeline.dto;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

//@Document(indexName = "text")
@Data
public class KafkaConsumerData {
    private Fields fields;
    private String name;
    private Tags tags;
    private String timestamp;


    @Data
    static public class Fields{
        private String message;
    }

    @Data
    static public class Tags{
        private String host;
        private String path;
    }
}
