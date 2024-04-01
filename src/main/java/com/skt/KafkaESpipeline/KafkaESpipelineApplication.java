package com.skt.KafkaESpipeline;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;

@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class KafkaESpipelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaESpipelineApplication.class, args);
	}

}
