package com.skt.KafkaESpipeline.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;

import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.AbstractOpenSearchConfiguration;
import org.opensearch.data.client.orhlc.ClientConfiguration;
import org.opensearch.data.client.orhlc.RestClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class ElasticSearchConfig extends AbstractOpenSearchConfiguration {

    @Value("${opensearch.address}")
    private String elasticsearchAddress;

    @Value("${opensearch.protocol}")
    private String protocol;


    @Override
    public RestHighLevelClient opensearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchAddress).build();

        return RestClients.create(clientConfiguration).rest();
    }
}