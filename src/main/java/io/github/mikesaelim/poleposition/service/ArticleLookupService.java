package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;

import java.time.LocalDate;
import java.util.List;

/**
 * Retrieve article metadata from MySQL.
 */
public interface ArticleLookupService {

    /**
     * Retrieve a single record.
     *
     * @param identifier article identifier
     * @return the article metadata if it is in MySQL, otherwise {@code null}
     */
    ArticleMetadata retrieveRecord(String identifier);

    /**
     * Retrieve a list of records for a given primary category, with original submission times that fall within the
     * acceptance window corresponding to a given day.  The returned list is sorted by submission time.
     *
     * @param primaryCategory primary category
     * @param day any date
     * @return list of article metadata records sorted by original submission time
     */
    List<ArticleMetadata> retrieveRecordsFor(String primaryCategory, LocalDate day);

}
