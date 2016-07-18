package io.github.mikesaelim.poleposition.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleVersion;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticleVersionPersistence;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.util.Set;

import static org.junit.Assert.*;

public class ArticleMapperTest {

    @Test
    public void testToArticlePersistence() throws Exception {
        ArticleVersion version1 = ArticleVersion.builder().versionNumber(1).submissionTime(ZonedDateTime.now()).build();
        ArticleVersion version2 = ArticleVersion.builder().versionNumber(2).submissionTime(ZonedDateTime.now()).build();

        ArticleMetadata domain = ArticleMetadata.builder()
                .retrievalDateTime(ZonedDateTime.of(2016, 7, 18, 6, 38, 22, 0, ZoneId.of("America/Chicago")))
                .identifier("identifier")
                .datestamp(LocalDate.of(2016, 7, 18))
                .sets(null)
                .deleted(false)
                .id("id")
                .submitter("submitter")
                .versions(Sets.newHashSet(version1, version2))
                .title("title")
                .authors("authors")
                .categories(Lists.newArrayList("cat1", "cat2", "cat3"))
                .comments("comments")
                .proxy("proxy")
                .reportNo("reportNo")
                .acmClass("acmClass")
                .mscClass("mscClass")
                .journalRef("journalRef")
                .doi("doi")
                .license("license")
                .articleAbstract("abstract")
                .build();

        ArticlePersistence persistence = ArticleMapper.toPersistence(domain);

        assertEquals("identifier", persistence.getIdentifier());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2016, 7, 18, 11, 38, 22, 0)), persistence.getRetrievalDateTimeUtc());
        assertEquals(Date.valueOf(LocalDate.of(2016, 7, 18)), persistence.getDatestamp());
        assertNull(persistence.getSets());
        assertFalse(persistence.isDeleted());
        assertEquals("id", persistence.getId());
        assertEquals("submitter", persistence.getSubmitter());
        assertEquals("title", persistence.getTitle());
        assertEquals("authors", persistence.getAuthors());
        assertEquals("cat1,cat2,cat3", persistence.getCategories());
        assertEquals("comments", persistence.getComments());
        assertEquals("proxy", persistence.getProxy());
        assertEquals("reportNo", persistence.getReportNo());
        assertEquals("acmClass", persistence.getAcmClass());
        assertEquals("mscClass", persistence.getMscClass());
        assertEquals("journalRef", persistence.getJournalRef());
        assertEquals("doi", persistence.getDoi());
        assertEquals("license", persistence.getLicense());
        assertEquals("abstract", persistence.getArticleAbstract());
        assertEquals(2, persistence.getVersions().size());
    }

    @Test
    public void testFromArticlePersistence() throws Exception {
        ArticleVersionPersistence versionPersistence1 = ArticleVersionPersistence.builder()
                .versionNumber(1).submissionTimeUtc(Timestamp.valueOf(LocalDateTime.now())).build();
        ArticleVersionPersistence versionPersistence2 = ArticleVersionPersistence.builder()
                .versionNumber(2).submissionTimeUtc(Timestamp.valueOf(LocalDateTime.now())).build();

        ArticlePersistence persistence = ArticlePersistence.builder()
                .identifier("identifier")
                .retrievalDateTimeUtc(Timestamp.valueOf(LocalDateTime.of(2016, 7, 18, 11, 38, 22, 0)))
                .datestamp(Date.valueOf(LocalDate.of(2016, 7, 18)))
                .sets(null)
                .deleted(false)
                .id("id")
                .submitter("submitter")
                .title("title")
                .authors("authors")
                .categories("cat1,cat2")
                .comments("comments")
                .proxy("proxy")
                .reportNo("reportNo")
                .acmClass("acmClass")
                .mscClass("mscClass")
                .journalRef("journalRef")
                .doi("doi")
                .license("license")
                .articleAbstract("abstract")
                .versions(Sets.newHashSet(versionPersistence1, versionPersistence2))
                .build();

        ArticleMetadata domain = ArticleMapper.fromPersistence(persistence);

        assertEquals(ZonedDateTime.of(2016, 7, 18, 11, 38, 22, 0, ZoneOffset.UTC), domain.getRetrievalDateTime());
        assertEquals("identifier", domain.getIdentifier());
        assertEquals(LocalDate.of(2016, 7, 18), domain.getDatestamp());
        assertNull(domain.getSets());
        assertFalse(domain.isDeleted());
        assertEquals("id", domain.getId());
        assertEquals("submitter", domain.getSubmitter());
        assertEquals(2, domain.getVersions().size());
        assertEquals("title", domain.getTitle());
        assertEquals("authors", domain.getAuthors());
        assertEquals(Lists.newArrayList("cat1", "cat2"), domain.getCategories());
        assertEquals("comments", domain.getComments());
        assertEquals("proxy", domain.getProxy());
        assertEquals("reportNo", domain.getReportNo());
        assertEquals("acmClass", domain.getAcmClass());
        assertEquals("mscClass", domain.getMscClass());
        assertEquals("journalRef", domain.getJournalRef());
        assertEquals("doi", domain.getDoi());
        assertEquals("license", domain.getLicense());
        assertEquals("abstract", domain.getArticleAbstract());
    }

    @Test
    public void testToVersionPersistence() throws Exception {
        ArticleVersion domain = ArticleVersion.builder()
                .versionNumber(1)
                .submissionTime(ZonedDateTime.of(2016, 7, 18, 6, 38, 22, 0, ZoneId.of("America/Chicago")))
                .size("size")
                .sourceType("sourceType")
                .build();

        ArticleVersionPersistence persistence = ArticleMapper.toPersistence(5, "identifier", domain);

        assertEquals(5, persistence.getId().intValue());
        assertEquals("identifier", persistence.getIdentifier());
        assertEquals(1, persistence.getVersionNumber().intValue());
        assertEquals(Timestamp.valueOf(LocalDateTime.of(2016, 7, 18, 11, 38, 22, 0)), persistence.getSubmissionTimeUtc());
        assertEquals("size", persistence.getSize());
        assertEquals("sourceType", persistence.getSourceType());
    }

    @Test
    public void testFromVersionPersistence() throws Exception {
        ArticleVersionPersistence persistence = ArticleVersionPersistence.builder()
                .id(5)
                .identifier("identifier")
                .versionNumber(1)
                .submissionTimeUtc(Timestamp.valueOf(LocalDateTime.of(2016, 7, 18, 11, 38, 22, 0)))
                .size("size")
                .sourceType("sourceType")
                .build();

        ArticleVersion domain = ArticleMapper.fromPersistence(persistence);

        assertEquals(1, domain.getVersionNumber().intValue());
        assertEquals(ZonedDateTime.of(2016, 7, 18, 11, 38, 22, 0, ZoneOffset.UTC), domain.getSubmissionTime());
        assertEquals("size", domain.getSize());
        assertEquals("sourceType", domain.getSourceType());
    }

}