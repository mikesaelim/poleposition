package io.github.mikesaelim.poleposition.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleVersion;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticleVersionPersistence;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

import static io.github.mikesaelim.poleposition.service.TimeUtils.convertFromUtcTimestamp;
import static io.github.mikesaelim.poleposition.service.TimeUtils.convertToUtcTimestamp;
import static java.util.stream.Collectors.toSet;

/**
 * Functions for mapping between domain-layer objects and persistence-layer objects.
 *
 * Even though the public functions could be made static, I've made them non-static and made this a service bean so
 * things can be mocked out during testing.
 */
@Service
public class ArticleMapper {

    // Conversions between ArticleMetadata and ArticlePersistence

    public ArticlePersistence toPersistence(ArticleMetadata domain) {
        ArticleVersion firstVersion = domain.getVersions().stream()
                .filter(v -> v.getVersionNumber().equals(1))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No version 1 of article " + domain.getIdentifier()));

        ArticlePersistence persistence = new ArticlePersistence();
        persistence.setIdentifier(domain.getIdentifier());
        persistence.setPrimaryCategory(domain.getCategories().get(0));
        persistence.setSubmissionTimeUtc(convertToUtcTimestamp(firstVersion.getSubmissionTime()));
        persistence.setRetrievalDateTimeUtc(convertToUtcTimestamp(domain.getRetrievalDateTime()));
        persistence.setDatestamp(Date.valueOf(domain.getDatestamp()));
        persistence.setSets(domain.getSets() != null ? String.join(",", domain.getSets()) : null);
        persistence.setDeleted(domain.isDeleted());
        persistence.setId(domain.getId());
        persistence.setSubmitter(domain.getSubmitter());
        persistence.setTitle(domain.getTitle());
        persistence.setAuthors(domain.getAuthors());
        persistence.setCategories(String.join(",", domain.getCategories()));
        persistence.setComments(domain.getComments());
        persistence.setProxy(domain.getProxy());
        persistence.setReportNo(domain.getReportNo());
        persistence.setAcmClass(domain.getAcmClass());
        persistence.setMscClass(domain.getMscClass());
        persistence.setJournalRef(domain.getJournalRef());
        persistence.setDoi(domain.getDoi());
        persistence.setLicense(domain.getLicense());
        persistence.setArticleAbstract(domain.getArticleAbstract());
        persistence.setVersions(toPersistenceSet(null, domain.getIdentifier(), domain.getVersions()));

        return persistence;
    }

    public ArticleMetadata fromPersistence(ArticlePersistence persistence) {
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

    public ArticleVersionPersistence toPersistence(Integer id, String identifier, ArticleVersion domain) {
        ArticleVersionPersistence persistence = new ArticleVersionPersistence();
        persistence.setId(id);
        persistence.setIdentifier(identifier);
        persistence.setVersionNumber(domain.getVersionNumber());
        persistence.setSubmissionTimeUtc(convertToUtcTimestamp(domain.getSubmissionTime()));
        persistence.setSize(domain.getSize());
        persistence.setSourceType(domain.getSourceType());

        return persistence;
    }

    public ArticleVersion fromPersistence(ArticleVersionPersistence persistence) {
        return ArticleVersion.builder()
                .versionNumber(persistence.getVersionNumber())
                .submissionTime(convertFromUtcTimestamp(persistence.getSubmissionTimeUtc()))
                .size(persistence.getSize())
                .sourceType(persistence.getSourceType())
                .build();
    }


    // Conversions between Set<ArticleVersion> and Set<ArticleVersionPersistence>

    public Set<ArticleVersionPersistence> toPersistenceSet(Integer id, String identifier,
                                                                  Set<ArticleVersion> domainSet) {
        if (domainSet == null)  return null;

        return domainSet.stream()
                .map(v -> toPersistence(id, identifier, v))
                .collect(toSet());
    }

    public Set<ArticleVersion> fromPersistenceSet(Set<ArticleVersionPersistence> persistenceSet) {
        if (persistenceSet == null)  return null;

        return persistenceSet.stream()
                .map(this::fromPersistence)
                .collect(toSet());
    }



}
