package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;

import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * Ingest article metadata from the arXiv OAI repository and persist it in MySQL.
 */
public interface ArxivIngestionService {

    /**
     * Ingest one record.
     *
     * @param identifier article identifier
     * @return true if the record was found
     * @throws URISyntaxException if the identifier results in an invalid URI for the harvester call
     * @throws TimeoutException if the indicated retry wait has exceeded the maximum
     * @throws RuntimeException for any other harvester errors
     */
    boolean ingestRecord(String identifier) throws URISyntaxException;

    /**
     * Ingest all the records updated since a given date.
     *
     * @param fromDate optional starting date of the ingestion period
     * @param setSpec optional arXiv set to constrain the ingestion to
     * @return the number of records ingested
     * @throws URISyntaxException if the fromDate or setSpec results in an invalid URI for the harvester call
     * @throws TimeoutException if the indicated retry wait has exceeded the maximum
     * @throws RuntimeException for any other harvester errors
     */
    int ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException;

}
