package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.service.ArticleLookupService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestWebConfig.class})
@WebAppConfiguration
public class ArticleControllerTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private ArticleLookupService articleLookupService;

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private static final MediaType CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private static final String IDENTIFIER = "oai:arXiv.org:1302.2146";
    private static final ArticleMetadata RECORD = ArticleMetadata.builder().identifier(IDENTIFIER).build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.reset(articleLookupService);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testRetrieveRecordByIdentifier() throws Exception {
        when(articleLookupService.retrieveRecord(IDENTIFIER)).thenReturn(RECORD);

        mockMvc.perform(get("/records/" + IDENTIFIER).accept(CONTENT_TYPE))
                .andExpect(status().isOk());

        // TODO validate JSON response body
    }

    @Test
    public void testRetrieveRecordByIdentifier_NotFound() throws Exception {
        when(articleLookupService.retrieveRecord(IDENTIFIER)).thenReturn(null);

        mockMvc.perform(get("/records/" + IDENTIFIER).accept(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

}