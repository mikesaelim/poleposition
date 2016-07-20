package io.github.mikesaelim.poleposition.web;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.service.ArticleLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
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
    @RequestMapping(value = "/records/{identifier}", method = RequestMethod.GET)
    ResponseEntity<ArticleMetadata> retrieveRecordByIdentifier(@PathVariable String identifier) {
        ArticleMetadata record = articleLookupService.retrieveRecord(identifier);

        return record == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(record);
    }

}
