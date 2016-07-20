package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ArticleLookupServiceImplTest {

    @Mock
    private ArticlePersistenceRepository repository;
    @Mock
    private ArticleMapper mapper;

    @InjectMocks
    private ArticleLookupServiceImpl service;

    private static final String IDENTIFIER = "oai:arXiv.org:1302.2146";

    private ArticlePersistence persistence;
    private ArticleMetadata article;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        persistence = new ArticlePersistence();
        article = ArticleMetadata.builder().build();

        when(mapper.fromPersistence(persistence)).thenReturn(article);
    }

    @Test
    public void testRetrieveRecord() throws Exception {
        when(repository.findOne(IDENTIFIER)).thenReturn(persistence);

        assertEquals(article, service.retrieveRecord(IDENTIFIER));
    }

    @Test
    public void testRetrieveRecord_NotFound() throws Exception {
        when(repository.findOne(IDENTIFIER)).thenReturn(null);

        assertNull(service.retrieveRecord(IDENTIFIER));
    }

}