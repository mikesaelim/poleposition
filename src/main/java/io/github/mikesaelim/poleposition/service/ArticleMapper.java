package io.github.mikesaelim.poleposition.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleVersion;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticleVersionPersistence;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Static functions for mapping between domain-layer objects and persistence-layer objects.
 */
public class ArticleMapper {

    // Conversions between ArticleMetadata and ArticlePersistence

    public static ArticlePersistence toPersistence(ArticleMetadata domain) {
        return ArticlePersistence.builder()
                .identifier(domain.getIdentifier())
                .retrievalDateTimeUtc(convertToUtcTimestamp(domain.getRetrievalDateTime()))
                .datestamp(Date.valueOf(domain.getDatestamp()))
                .sets(domain.getSets() != null ? String.join(",", domain.getSets()) : null)
                .deleted(domain.isDeleted())
                .id(domain.getId())
                .submitter(domain.getSubmitter())
                .title(domain.getTitle())
                .authors(domain.getAuthors())
                .categories(domain.getCategories() != null ? String.join(",", domain.getCategories()) : null)
                .comments(domain.getComments())
                .proxy(domain.getProxy())
                .reportNo(domain.getReportNo())
                .acmClass(domain.getAcmClass())
                .mscClass(domain.getMscClass())
                .journalRef(domain.getJournalRef())
                .doi(domain.getDoi())
                .license(domain.getLicense())
                .articleAbstract(domain.getArticleAbstract())
                .versions(toPersistenceSet(null, domain.getIdentifier(), domain.getVersions()))
                .build();
    }

    public static ArticleMetadata fromPersistence(ArticlePersistence persistence) {
        return ArticleMetadata.builder()
                .retrievalDateTime(convertFromUtcTimestamp(persistence.getRetrievalDateTimeUtc()))
                .identifier(persistence.getIdentifier())
                .datestamp(persistence.getDatestamp().toLocalDate())
                .sets(persistence.getSets() != null ? Sets.newHashSet(persistence.getSets().split(",")) : null)
                .deleted(persistence.isDeleted())
                .id(persistence.getId())
                .submitter(persistence.getSubmitter())
                .versions(fromPersistenceSet(persistence.getVersions()))
                .title(persistence.getTitle())
                .authors(persistence.getAuthors())
                .categories(persistence.getCategories() != null ? Lists.newArrayList(persistence.getCategories().split(",")) : null)
                .comments(persistence.getComments())
                .proxy(persistence.getProxy())
                .reportNo(persistence.getReportNo())
                .acmClass(persistence.getAcmClass())
                .mscClass(persistence.getMscClass())
                .journalRef(persistence.getJournalRef())
                .doi(persistence.getDoi())
                .license(persistence.getLicense())
                .articleAbstract(persistence.getArticleAbstract())
                .build();
    }


    // Conversions between ArticleVersion and ArticleVersionPersistence

    public static ArticleVersionPersistence toPersistence(Integer id, String identifier, ArticleVersion domain) {
        return ArticleVersionPersistence.builder()
                .id(id)
                .identifier(identifier)
                .versionNumber(domain.getVersionNumber())
                .submissionTimeUtc(convertToUtcTimestamp(domain.getSubmissionTime()))
                .size(domain.getSize())
                .sourceType(domain.getSourceType())
                .build();
    }

    public static ArticleVersion fromPersistence(ArticleVersionPersistence persistence) {
        return ArticleVersion.builder()
                .versionNumber(persistence.getVersionNumber())
                .submissionTime(convertFromUtcTimestamp(persistence.getSubmissionTimeUtc()))
                .size(persistence.getSize())
                .sourceType(persistence.getSourceType())
                .build();
    }


    // Conversions between Set<ArticleVersion> and Set<ArticleVersionPersistence>

    public static Set<ArticleVersionPersistence> toPersistenceSet(Integer id, String identifier,
                                                                  Set<ArticleVersion> domainSet) {
        if (domainSet == null)  return null;

        return domainSet.stream()
                .map(v -> toPersistence(id, identifier, v))
                .collect(toSet());
    }

    public static Set<ArticleVersion> fromPersistenceSet(Set<ArticleVersionPersistence> persistenceSet) {
        if (persistenceSet == null)  return null;

        return persistenceSet.stream()
                .map(ArticleMapper::fromPersistence)
                .collect(toSet());
    }


    // Conversions between ZonedDateTime and Timestamp

    private static Timestamp convertToUtcTimestamp(ZonedDateTime zonedDateTime) {
        return Timestamp.valueOf(zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }

    private static ZonedDateTime convertFromUtcTimestamp(Timestamp timestamp) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC);
    }

}
