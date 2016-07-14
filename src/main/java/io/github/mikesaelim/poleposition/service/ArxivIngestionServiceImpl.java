package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import io.github.mikesaelim.arxivoaiharvester.model.request.GetRecordRequest;
import io.github.mikesaelim.arxivoaiharvester.model.request.ListRecordsRequest;
import io.github.mikesaelim.arxivoaiharvester.model.response.GetRecordResponse;
import io.github.mikesaelim.arxivoaiharvester.model.response.ListRecordsResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.LocalDate;

@Service
public class ArxivIngestionServiceImpl implements ArxivIngestionService {

    @Autowired
    private ArxivOAIHarvester harvester;

    @Override
    public void ingestRecord(@NonNull String identifier) throws URISyntaxException {
        GetRecordRequest request = new GetRecordRequest(identifier);
        GetRecordResponse response = harvester.harvest(request);

        // TODO make this persist to MySQL
        System.out.println("Returned record:");
        System.out.println(response.getRecord().toString());
    }

    @Override
    public void ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException {
        ListRecordsRequest request = new ListRecordsRequest(fromDate, null, setSpec);
        while (request != ListRecordsRequest.NONE) {
            ListRecordsResponse response = harvester.harvest(request);

            // TODO make this persist to MySQL
            System.out.println("Returned page of size " + response.getRecords().size());

            request = response.resumption();
        }
    }

}
