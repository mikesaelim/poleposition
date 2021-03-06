package io.github.mikesaelim.poleposition.service;

import com.google.common.collect.ImmutableList;
import io.github.mikesaelim.arxivoaiharvester.ArxivOAIHarvester;
import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.arxivoaiharvester.model.request.GetRecordRequest;
import io.github.mikesaelim.arxivoaiharvester.model.request.ListRecordsRequest;
import io.github.mikesaelim.arxivoaiharvester.model.response.GetRecordResponse;
import io.github.mikesaelim.arxivoaiharvester.model.response.ListRecordsResponse;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ArxivIngestionServiceImplTest {

    @Mock
    private ArxivOAIHarvester harvester;
    @Mock
    private ArticleMapper mapper;
    @Mock
    private ArticlePersistenceRepository repository;

    @InjectMocks
    private ArxivIngestionServiceImpl service;

    private ArticleMetadata article1;
    private ArticleMetadata article2;
    private ArticlePersistence persistence1;
    private ArticlePersistence persistence2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        article1 = ArticleMetadata.builder().identifier("article1").build();
        article2 = ArticleMetadata.builder().identifier("article2").build();

        persistence1 = new ArticlePersistence();
        persistence1.setIdentifier("article1");
        persistence2 = new ArticlePersistence();
        persistence2.setIdentifier("article2");

        when(mapper.toPersistence(article1)).thenReturn(persistence1);
        when(mapper.toPersistence(article2)).thenReturn(persistence2);
    }

    @Test
    public void testIngestRecord() throws Exception {
        getRecordFinds(article1);

        boolean found = service.ingestRecord("mock_identifier");

        assertTrue(found);
        verify(repository).save(persistence1);
    }

    @Test
    public void testIngestRecord_NotFound() throws Exception {
        getRecordFinds(null);

        boolean found = service.ingestRecord("mock_identifier");

        assertFalse(found);
        verifyZeroInteractions(repository);
    }

    @Test(expected = TimeoutException.class)
    public void testIngestRecord_TimeoutException() throws Exception {
        getRecordTimesOut();

        service.ingestRecord("mock_identifier");
    }

    @Test(expected = RuntimeException.class)
    public void testIngestRecord_RuntimeException() throws Exception {
        getRecordErrorsOut();

        service.ingestRecord("mock_identifier");
    }

    @Test
    public void testIngestMetadataSince() throws Exception {
        listRecordsFinds(ImmutableList.of(article1, article2));

        int numRecords = service.ingestMetadataSince(LocalDate.now(), null);

        assertEquals(2, numRecords);
        verify(repository).save(persistence1);
        verify(repository).save(persistence2);
    }

    @Test
    public void testIngestMetadataSince_NotFound() throws Exception {
        listRecordsFinds(ImmutableList.of());

        int numRecords = service.ingestMetadataSince(LocalDate.now(), null);

        assertEquals(0, numRecords);
        verifyZeroInteractions(repository);
    }

    @Test(expected = TimeoutException.class)
    public void testIngestMetadataSince_TimeoutException() throws Exception {
        listRecordsTimesOut();

        service.ingestMetadataSince(LocalDate.now(), null);
    }

    @Test(expected = RuntimeException.class)
    public void testIngestMetadataSince_RuntimeException() throws Exception {
        listRecordsErrorsOut();

        service.ingestMetadataSince(LocalDate.now(), null);
    }

    private void getRecordFinds(ArticleMetadata articleMetadata) {
        when(harvester.harvest(any(GetRecordRequest.class)))
                .thenAnswer(invocation -> GetRecordResponse.builder()
                        .responseDate(ZonedDateTime.now())
                        .request(invocation.getArgumentAt(0, GetRecordRequest.class))
                        .record(articleMetadata)
                        .build());
    }

    private void getRecordTimesOut() {
        when(harvester.harvest(any(GetRecordRequest.class))).thenThrow(new TimeoutException());
    }

    private void getRecordErrorsOut() {
        when(harvester.harvest(any(GetRecordRequest.class))).thenThrow(new RuntimeException());
    }

    private void listRecordsFinds(ImmutableList<ArticleMetadata> articleMetadataList) {
        when(harvester.harvest(any(ListRecordsRequest.class)))
                .thenAnswer(invocation -> ListRecordsResponse.builder()
                        .responseDate(ZonedDateTime.now())
                        .request(invocation.getArgumentAt(0, ListRecordsRequest.class))
                        .records(articleMetadataList)
                        .resumptionToken(null)
                        .build());
    }

    private void listRecordsTimesOut() {
        when(harvester.harvest(any(ListRecordsRequest.class))).thenThrow(new TimeoutException());
    }

    private void listRecordsErrorsOut() {
        when(harvester.harvest(any(ListRecordsRequest.class))).thenThrow(new RuntimeException());
    }

}