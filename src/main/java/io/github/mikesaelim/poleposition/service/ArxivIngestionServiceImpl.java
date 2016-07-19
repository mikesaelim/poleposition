package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import io.github.mikesaelim.arxivoaiharvester.model.request.GetRecordRequest;
import io.github.mikesaelim.arxivoaiharvester.model.request.ListRecordsRequest;
import io.github.mikesaelim.arxivoaiharvester.model.response.GetRecordResponse;
import io.github.mikesaelim.arxivoaiharvester.model.response.ListRecordsResponse;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDate;

import static io.github.mikesaelim.poleposition.service.ArticleMapper.toPersistence;

/**
 * TODO javadoc, exception handling
 */
@Service
public class ArxivIngestionServiceImpl implements ArxivIngestionService {

    @Autowired
    private ArxivOAIHarvester harvester;

    @Autowired
    private ArticlePersistenceRepository repository;

    @Override
    public void ingestRecord(@NonNull String identifier) throws URISyntaxException {
        GetRecordRequest request = new GetRecordRequest(identifier);
        GetRecordResponse response = harvester.harvest(request);

        if (response.getRecord() != null) {
            repository.save(toPersistence(response.getRecord()));
            System.out.println("Record for identifier = " + identifier + " saved.");
        } else {
            System.out.println("Record for identifier = " + identifier + " not found.");
        }
    }

    @Override
    public void ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException {
        ListRecordsRequest request = new ListRecordsRequest(fromDate, null, setSpec);
        while (request != ListRecordsRequest.NONE) {
            ListRecordsResponse response = harvester.harvest(request);

            response.getRecords().stream()
                    .map(ArticleMapper::toPersistence)
                    .forEach(repository::save);

            System.out.println("Saved " + response.getRecords().size() + " records.");

            request = response.resumption();
        }
    }

}
