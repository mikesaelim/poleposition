package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ComponentScan
public class ServiceConfig {

    @Bean
    public ArxivOAIHarvester arxivOAIHarvester() {
        // TODO pass in config properties
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new ArxivOAIHarvester(httpClient, 3, Duration.ofSeconds(20), Duration.ofMinutes(5));
    }

}
