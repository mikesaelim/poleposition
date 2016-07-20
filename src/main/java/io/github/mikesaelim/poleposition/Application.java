package io.github.mikesaelim.poleposition;

import io.github.mikesaelim.poleposition.persistence.PersistenceConfig;
import io.github.mikesaelim.poleposition.service.ArxivIngestionService;
import io.github.mikesaelim.poleposition.service.ServiceConfig;
import io.github.mikesaelim.poleposition.web.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({WebConfig.class, ServiceConfig.class, PersistenceConfig.class})
@SpringBootApplication
public class Application {

    @Autowired
    ArxivIngestionService arxivIngestionService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

}
