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

/**
 * Note that this implementation does not handle any of the unchecked exceptions coming from the harvester, since there
 * is nothing really special to do for them - the ingestion has failed and it should be reported back to the user,
 * which happens regardless if we catch the unchecked exceptions or not.
 *
 * When we upgrade this project to being a fully running service, instead of just an on-demand ETL job, we will need
 * to handle the exceptions.
 */
@Service
public class ArxivIngestionServiceImpl implements ArxivIngestionService {

    @Autowired
    private ArxivOAIHarvester harvester;
    @Autowired
    private ArticleMapper mapper;
    @Autowired
    private ArticlePersistenceRepository repository;

    @Override
    public void ingestRecord(@NonNull String identifier) throws URISyntaxException {
        GetRecordRequest request = new GetRecordRequest(identifier);
        GetRecordResponse response = harvester.harvest(request);

        if (response.getRecord() != null) {
            repository.save(mapper.toPersistence(response.getRecord()));
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
                    .map(mapper::toPersistence)
                    .forEach(repository::save);

            System.out.println("Saved " + response.getRecords().size() + " records.");

            request = response.resumption();
        }
    }

}
