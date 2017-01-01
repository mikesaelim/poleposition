package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;
import io.github.mikesaelim.poleposition.service.ArxivIngestionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IngestionController.class)
public class IngestionControllerTest {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArxivIngestionService arxivIngestionService;

    private static final String IDENTIFIER = "oai:arXiv.org:1302.2146";
    private static final LocalDate FROM_DATE = LocalDate.of(2016, 7, 20);
    private static final String FROM_STRING = "2016-07-20";
    private static final String SET = "physics:hep-ph";

    @Before
    public void setUp() throws Exception {
        Mockito.reset(arxivIngestionService);
    }

    @Test
    public void testIngestRecordByIdentifier() throws Exception {
        when(arxivIngestionService.ingestRecord(IDENTIFIER)).thenReturn(true);

        mockMvc.perform(put("/records/" + IDENTIFIER))
                .andExpect(status().isOk());
    }

    @Test
    public void testIngestRecordByIdentifier_NotFound() throws Exception {
        when(arxivIngestionService.ingestRecord(IDENTIFIER)).thenReturn(false);

        mockMvc.perform(put("/records/" + IDENTIFIER))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testIngestRecordByIdentifier_URISyntaxException() throws Exception {
        when(arxivIngestionService.ingestRecord(IDENTIFIER)).thenThrow(new URISyntaxException("i", "r"));

        mockMvc.perform(put("/records/" + IDENTIFIER))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIngestRecordByIdentifier_TimeoutException() throws Exception {
        when(arxivIngestionService.ingestRecord(IDENTIFIER)).thenThrow(new TimeoutException());

        mockMvc.perform(put("/records/" + IDENTIFIER))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void testIngestRecordByIdentifier_Exception() throws Exception {
        when(arxivIngestionService.ingestRecord(IDENTIFIER)).thenThrow(new RuntimeException());

        mockMvc.perform(put("/records/" + IDENTIFIER))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testIngestRecordsSince() throws Exception {
        when(arxivIngestionService.ingestMetadataSince(FROM_DATE, SET)).thenReturn(5);

        mockMvc.perform(put("/records?from=" + FROM_STRING + "&set=" + SET))
                .andExpect(status().isOk());
    }

    @Test
    public void testIngestRecordsSince_NoFrom() throws Exception {
        mockMvc.perform(put("/records?set=" + SET))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(arxivIngestionService);
    }

    @Test
    public void testIngestRecordsSince_NoSet() throws Exception {
        mockMvc.perform(put("/records?from=" + FROM_STRING))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(arxivIngestionService);
    }

    @Test
    public void testIngestRecordsSince_BadFromDate() throws Exception {
        mockMvc.perform(put("/records?from=2016-0720&set=" + SET))
                .andExpect(status().isBadRequest());

        verifyZeroInteractions(arxivIngestionService);
    }

    @Test
    public void testIngestRecordsSince_URISyntaxException() throws Exception {
        when(arxivIngestionService.ingestMetadataSince(FROM_DATE, SET)).thenThrow(new URISyntaxException("i", "r"));

        mockMvc.perform(put("/records?from=" + FROM_STRING + "&set=" + SET))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testIngestRecordsSince_TimeoutException() throws Exception {
        when(arxivIngestionService.ingestMetadataSince(FROM_DATE, SET)).thenThrow(new TimeoutException());

        mockMvc.perform(put("/records?from=" + FROM_STRING + "&set=" + SET))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void testIngestRecordsSince_Exception() throws Exception {
        when(arxivIngestionService.ingestMetadataSince(FROM_DATE, SET)).thenThrow(new RuntimeException());

        mockMvc.perform(put("/records?from=" + FROM_STRING + "&set=" + SET))
                .andExpect(status().isInternalServerError());
    }

}