package io.github.mikesaelim.poleposition.service;

import com.google.common.collect.Lists;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ArticleLookupServiceImplTest {

    @Mock
    private ArticlePersistenceRepository repository;
    @Mock
    private ArticleMapper mapper;
    @Mock
    private AcceptanceWindowCalculator acceptanceWindowCalculator;

    @InjectMocks
    private ArticleLookupServiceImpl service;

    private static final String IDENTIFIER = "oai:arXiv.org:1302.2146";
    private ArticlePersistence persistence1;
    private ArticlePersistence persistence2;
    private ArticleMetadata article1;
    private ArticleMetadata article2;

    private static final String PRIMARY_CATEGORY = "cat";
    private static final LocalDate DAY = LocalDate.of(2016, 4, 1);
    private static final ZonedDateTime START_ZDT = ZonedDateTime.of(2016, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    private static final ZonedDateTime END_ZDT = ZonedDateTime.of(2016, 2, 2, 2, 2, 2, 2, ZoneOffset.UTC);
    private static final Timestamp START_TIMESTAMP = Timestamp.valueOf(LocalDateTime.of(2016, 1, 1, 1, 1, 1, 1));
    private static final Timestamp END_TIMESTAMP = Timestamp.valueOf(LocalDateTime.of(2016, 2, 2, 2, 2, 2, 2));

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(acceptanceWindowCalculator.acceptanceWindowFor(DAY)).thenReturn(new AcceptanceWindow(START_ZDT, END_ZDT));

        persistence1 = new ArticlePersistence();
        persistence1.setIdentifier("1");
        persistence2 = new ArticlePersistence();
        persistence2.setIdentifier("2");

        article1 = ArticleMetadata.builder().identifier("1").build();
        article2 = ArticleMetadata.builder().identifier("2").build();

        when(mapper.fromPersistence(persistence1)).thenReturn(article1);
        when(mapper.fromPersistence(persistence2)).thenReturn(article2);
    }

    @Test
    public void testRetrieveRecord() throws Exception {
        when(repository.findOne(IDENTIFIER)).thenReturn(persistence1);

        assertEquals(article1, service.retrieveRecord(IDENTIFIER));
    }

    @Test
    public void testRetrieveRecord_NotFound() throws Exception {
        when(repository.findOne(IDENTIFIER)).thenReturn(null);

        assertNull(service.retrieveRecord(IDENTIFIER));
    }

    @Test
    public void testRetrieveRecordsFor() throws Exception {
        when(repository.findByPrimaryCategoryAndSubmissionTime(PRIMARY_CATEGORY, START_TIMESTAMP, END_TIMESTAMP))
                .thenReturn(Lists.newArrayList(persistence1, persistence2));

        List<ArticleMetadata> result = service.retrieveRecordsFor(PRIMARY_CATEGORY, DAY);

        assertEquals(2, result.size());
        assertEquals(article1, result.get(0));
        assertEquals(article2, result.get(1));
    }

    @Test(expected = NoAcceptanceWindowException.class)
    public void testRetrieveRecordsFor_NoAcceptanceWindow() throws Exception {
        when(acceptanceWindowCalculator.acceptanceWindowFor(DAY)).thenReturn(null);

        service.retrieveRecordsFor(PRIMARY_CATEGORY, DAY);
    }

    @Test
    public void testRetrieveRecordsFor_NoneFound() throws Exception {
        when(repository.findByPrimaryCategoryAndSubmissionTime(PRIMARY_CATEGORY, START_TIMESTAMP, END_TIMESTAMP))
                .thenReturn(Lists.newArrayList());

        List<ArticleMetadata> result = service.retrieveRecordsFor(PRIMARY_CATEGORY, DAY);

        assertTrue(result.isEmpty());
    }

}