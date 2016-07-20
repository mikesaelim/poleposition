package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.poleposition.service.ArticleLookupService;
import io.github.mikesaelim.poleposition.service.ArxivIngestionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@Import(WebConfig.class)
@Configuration
public class TestWebConfig {

    /**
     * For injecting into our test controllers.
     */
    @Bean
    public ArxivIngestionService arxivIngestionService() {
        return mock(ArxivIngestionService.class);
    }

    @Bean
    public ArticleLookupService articleLookupService() {
        return mock(ArticleLookupService.class);
    }

}
