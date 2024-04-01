package com.skt.KafkaESpipeline.config;


import lombok.extern.slf4j.Slf4j;

import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.AbstractOpenSearchConfiguration;
import org.opensearch.data.client.orhlc.ClientConfiguration;
import org.opensearch.data.client.orhlc.RestClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OpenSearchConfig extends AbstractOpenSearchConfiguration {

    @Value("${opensearch.address}")
    private String elasticsearchAddress;

    @Override
    public RestHighLevelClient opensearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchAddress).build();

        return RestClients.create(clientConfiguration).rest();
    }
}