package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.service.ArticleLookupService;
import io.github.mikesaelim.poleposition.service.NoAcceptanceWindowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/records")
public class ArticleController {

    @Autowired
    ArticleLookupService articleLookupService;

    /**
     * Retrieve one record.
     *
     * @param identifier article identifier
     * @return 200 OK if the record was found
     *         404 Not Found if the record could not be found
     */
    @GetMapping("/{identifier}")
    ResponseEntity<ArticleMetadata> retrieveRecordByIdentifier(@PathVariable String identifier) {
        ArticleMetadata record = articleLookupService.retrieveRecord(identifier);

        return record == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(record);
    }

    /**
     * Retrieve the records for a given primary category, with original submission times that fall within the
     * acceptance window corresponding to a given day.  The returned list is sorted by submission time.
     *
     * @param primaryCategory primary category
     * @param dayString day of submission, in ISO 8601 format
     * @return 200 OK and the list of records sorted by submission time, or an empty list if there are none
     *         204 No Content if no valid acceptance window starts on that day
     *         400 Bad Request if either the primary category or day are missing
     *         400 Bad Request if the day is not in ISO 8601 format
     */
    @GetMapping
    ResponseEntity<List<ArticleMetadata>> retrieveRecordsByPrimaryCategoryAndDay(
            @RequestParam("category") String primaryCategory, @RequestParam("day") String dayString) {
        LocalDate day;
        try {
            day = LocalDate.parse(dayString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            return ResponseEntity.ok(articleLookupService.retrieveRecordsFor(primaryCategory, day));
        } catch (NoAcceptanceWindowException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Retrieve the earliest submitted record for a given primary category, with an original submission time that falls
     * within the acceptance window corresponding to a given day.
     *
     * @param primaryCategory primary category
     * @param dayString day of submission, in ISO 8601 format
     * @return 200 OK and the earliest submitted record
     *         204 No Content if no records were submitted in this acceptance window and for this primary category
     *         204 No Content if no valid acceptance window starts on that day
     *         400 Bad Request if either the primary category or day are missing
     *         400 Bad Request if the day is not in ISO 8601 format
     */
    @GetMapping("/first")
    ResponseEntity<ArticleMetadata> retrieveFirstRecordByPrimaryCategoryAndDay(
            @RequestParam("category") String primaryCategory, @RequestParam("day") String dayString) {
        LocalDate day;
        try {
            day = LocalDate.parse(dayString);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            List<ArticleMetadata> records = articleLookupService.retrieveRecordsFor(primaryCategory, day);

            return records.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : ResponseEntity.ok(records.get(0));
        } catch (NoAcceptanceWindowException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
