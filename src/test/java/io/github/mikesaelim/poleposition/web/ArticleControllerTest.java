package io.github.mikesaelim.poleposition.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleVersion;
import io.github.mikesaelim.poleposition.service.ArticleLookupService;
import io.github.mikesaelim.poleposition.service.NoAcceptanceWindowException;
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

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private static final String PRIMARY_CATEGORY = "hep-ph";
    private static final LocalDate DAY = LocalDate.of(2013, 2, 8);
    private static final ArticleMetadata RECORD = buildRecord();

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
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.identifier", is(IDENTIFIER)))
                .andExpect(jsonPath("$.datestamp", is(Lists.newArrayList(2015, 6, 15))))
                .andExpect(jsonPath("$.sets", hasSize(2)))
                .andExpect(jsonPath("$.id", is(RECORD.getId())))
                .andExpect(jsonPath("$.submitter", is(RECORD.getSubmitter())))
                .andExpect(jsonPath("$.versions", hasSize(2)))
                .andExpect(jsonPath("$.title", is(RECORD.getTitle())))
                .andExpect(jsonPath("$.authors", is(RECORD.getAuthors())))
                .andExpect(jsonPath("$.categories", hasSize(2)))
                .andExpect(jsonPath("$.categories[0]", is(RECORD.getCategories().get(0))))
                .andExpect(jsonPath("$.categories[1]", is(RECORD.getCategories().get(1))))
                .andExpect(jsonPath("$.comments", is(RECORD.getComments())))
                .andExpect(jsonPath("$.doi", is(RECORD.getDoi())))
                .andExpect(jsonPath("$.license", is(RECORD.getLicense())))
                .andExpect(jsonPath("$.articleAbstract", is(RECORD.getArticleAbstract())));
    }

    @Test
    public void testRetrieveRecordByIdentifier_NotFound() throws Exception {
        when(articleLookupService.retrieveRecord(IDENTIFIER)).thenReturn(null);

        mockMvc.perform(get("/records/" + IDENTIFIER).accept(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRetrieveRecordsFor() throws Exception {
        when(articleLookupService.retrieveRecordsFor(PRIMARY_CATEGORY, DAY))
                .thenReturn(Lists.newArrayList(RECORD, RECORD));

        mockMvc.perform(get("/records?category=" + PRIMARY_CATEGORY + "&day=" + DAY.toString()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testRetrieveRecordsFor_NoneFound() throws Exception {
        when(articleLookupService.retrieveRecordsFor(PRIMARY_CATEGORY, DAY)).thenReturn(Lists.newArrayList());

        mockMvc.perform(get("/records?category=" + PRIMARY_CATEGORY + "&day=" + DAY.toString()).accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testRetrieveRecordsFor_NoAcceptanceWindow() throws Exception {
        when(articleLookupService.retrieveRecordsFor(PRIMARY_CATEGORY, DAY))
                .thenThrow(new NoAcceptanceWindowException());

        mockMvc.perform(get("/records?category=" + PRIMARY_CATEGORY + "&day=" + DAY.toString()).accept(CONTENT_TYPE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRetrieveRecordsFor_NoCategory() throws Exception {
        mockMvc.perform(get("/records?day=" + DAY.toString()).accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(articleLookupService);
    }

    @Test
    public void testRetrieveRecordsFor_NoDay() throws Exception {
        mockMvc.perform(get("/records?category=" + PRIMARY_CATEGORY).accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(articleLookupService);
    }

    @Test
    public void testRetrieveRecordsFor_InvalidDay() throws Exception {
        mockMvc.perform(get("/records?category=" + PRIMARY_CATEGORY + " &day=2016-07").accept(CONTENT_TYPE))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(articleLookupService);
    }

    private static ArticleMetadata buildRecord() {
        ArticleVersion v1 = ArticleVersion.builder()
                .versionNumber(1)
                .submissionTime(ZonedDateTime.of(2013, 2, 8, 21, 0, 1, 0, ZoneOffset.UTC))
                .size("853kb")
                .sourceType("D")
                .build();
        ArticleVersion v2 = ArticleVersion.builder()
                .versionNumber(2)
                .submissionTime(ZonedDateTime.of(2013, 4, 2, 1, 50, 8, 0, ZoneOffset.UTC))
                .size("849kb")
                .sourceType("D")
                .build();

        return ArticleMetadata.builder()
                .retrievalDateTime(ZonedDateTime.now())
                .identifier(IDENTIFIER)
                .datestamp(LocalDate.of(2015, 6, 15))
                .sets(Sets.newHashSet("physics:hep-ph", "physics:hep-ex"))
                .id("1302.2146")
                .submitter("Michael Saelim")
                .versions(Sets.newHashSet(v1, v2))
                .title("The Same-Sign Dilepton Signature of RPV/MFV SUSY")
                .authors("Joshua Berger, Maxim Perelstein, Michael Saelim and Philip Tanedo")
                .categories(Lists.newArrayList("hep-ph", "hep-ex"))
                .comments("18 pages, 6 figures; v2: References added")
                .doi("10.1007/JHEP04(2013)077")
                .license("http://arxiv.org/licenses/nonexclusive-distrib/1.0/")
                .articleAbstract("Blah blah blah blah")
                .build();
    }

}