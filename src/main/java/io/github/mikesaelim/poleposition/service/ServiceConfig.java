package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.time.Duration;

@Configuration
public class ServiceConfig {

    @Autowired
    Environment env;

    @Bean
    public ArxivOAIHarvester arxivOAIHarvester() {
        int maxRetries = Integer.valueOf(env.getProperty("harvester.maxRetries", "3"));
        int minWaitBetweenRequestsInSecs =
                Integer.valueOf(env.getProperty("harvester.minWaitBetweenRequestsInSecs", "30"));
        int maxWaitBetweenRequestsInMins =
                Integer.valueOf(env.getProperty("harvester.maxWaitBetweenRequestsInMins", "5"));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        return new ArxivOAIHarvester(httpClient, maxRetries,
                Duration.ofSeconds(minWaitBetweenRequestsInSecs), Duration.ofMinutes(maxWaitBetweenRequestsInMins));
    }

}
