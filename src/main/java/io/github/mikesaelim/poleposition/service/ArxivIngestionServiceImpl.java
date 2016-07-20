package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;
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

@Service
public class ArxivIngestionServiceImpl implements ArxivIngestionService {

    @Autowired
    private ArxivOAIHarvester harvester;
    @Autowired
    private ArticleMapper mapper;
    @Autowired
    private ArticlePersistenceRepository repository;

    @Override
    public boolean ingestRecord(@NonNull String identifier) throws URISyntaxException {
        GetRecordRequest request = new GetRecordRequest(identifier);

        GetRecordResponse response;
        try {
            response = harvester.harvest(request);
        } catch (TimeoutException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (response.getRecord() != null) {
            repository.save(mapper.toPersistence(response.getRecord()));
            return true;
        }

        return false;
    }

    @Override
    public int ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException {
        ListRecordsRequest request = new ListRecordsRequest(fromDate, null, setSpec);
        int numRecordsIngested = 0;

        while (request != ListRecordsRequest.NONE) {
            ListRecordsResponse response = harvester.harvest(request);

            response.getRecords().stream()
                    .map(mapper::toPersistence)
                    .forEach(repository::save);

            numRecordsIngested += response.getRecords().size();

            request = response.resumption();
        }

        return numRecordsIngested;
    }

}
