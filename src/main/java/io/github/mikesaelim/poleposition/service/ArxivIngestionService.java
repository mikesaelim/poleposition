package io.github.mikesaelim.poleposition.service;

import java.net.URISyntaxException;
import java.time.LocalDate;

/**
 * TODO javadoc
 */
public interface ArxivIngestionService {

    void ingestRecord(String identifier) throws URISyntaxException;

    void ingestMetadataSince(LocalDate fromDate, String setSpec) throws URISyntaxException;

}
