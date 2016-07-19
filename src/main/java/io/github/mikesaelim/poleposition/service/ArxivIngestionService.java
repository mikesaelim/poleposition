package io.github.mikesaelim.poleposition.service;

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
     * @throws URISyntaxException if the identifier results in an invalid URI for the harvester call
     */
    void ingestRecord(String identifier) throws URISyntaxException;

    /**
     * Ingest all the records updated since a given date.
     *
     * @param fromDate optional starting date of the ingestion period
     * @param setSpec optional arXiv set to constrain the ingestion to
     * @throws URISyntaxException if the fromDate or setSpec results in an invalid URI for the harvester call
     */
    void ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException;

}
