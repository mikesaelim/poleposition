package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;

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

}
