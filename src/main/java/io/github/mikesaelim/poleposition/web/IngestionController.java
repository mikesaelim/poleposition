package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.arxivoaiharvester.exception.TimeoutException;
import io.github.mikesaelim.poleposition.service.ArxivIngestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/")
public class IngestionController {

    @Autowired
    ArxivIngestionService ingestionService;

    /**
     * Ingest one record.
     *
     * @param identifier article identifier
     * @return 200 OK if the ingestion was successful
     *         400 Bad Request if the harvester URI was invalid
     *         404 Not Found if the record could not be found
     *         503 Service Unavailable if the operation timed out
     *         500 Internal Server Error for any other error
     */
    @RequestMapping(value = "/records/{identifier}", method = RequestMethod.PUT)
    ResponseEntity<String> ingestRecordByIdentifier(@PathVariable String identifier) {
        try {
            boolean found = ingestionService.ingestRecord(identifier);

            return found ? ResponseEntity.ok("Ingestion successful for " + identifier) :
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find record for " + identifier);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Could not create a valid harvester URI for " + identifier);
        } catch (TimeoutException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Ingest all the records since a given date, in a given set.  Both the starting date and set must be specified.
     *
     * While we are capable of ingesting records for all sets, or since the beginning, I don't want to allow it here
     * because the chances I'll accidentally kick off an all-records ingestion is too high.
     *
     * @param fromString starting date, in ISO 8601 format
     * @param setSpec article set
     * @return 200 OK if the ingestion was successful, or if no records were found
     *         400 Bad Request if the from date or set is missing, the date is invalid, or if the harvester URI was invalid
     *         503 Service Unavailable if the operation timed out
     *         500 Internal Server Error for any other error
     */
    @RequestMapping(value = "/records", method = RequestMethod.PUT)
    ResponseEntity<String> ingestRecordsSince(@RequestParam("from") String fromString,
                                              @RequestParam("set") String setSpec) {
        if (StringUtils.isBlank(fromString) || StringUtils.isBlank(setSpec)) {
            return ResponseEntity.badRequest().body("'from' and 'set' params required");
        }

        LocalDate fromDate;
        try {
            fromDate = LocalDate.parse(fromString);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Could not parse date " + fromString);
        }

        try {
            int numRecordsIngested = ingestionService.ingestMetadataSince(fromDate, setSpec);

            return ResponseEntity.ok("Ingested " + numRecordsIngested + " records");
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest()
                    .body("Could not create a valid harvester URI for from=" + fromString + " and set=" + setSpec);
        } catch (TimeoutException e) {
            // TODO should we communicate a partial success? include a counter of how many records got stored
            // TODO successfully, updated for each one?
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
