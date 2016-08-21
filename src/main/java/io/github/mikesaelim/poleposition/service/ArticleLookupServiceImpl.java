package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static io.github.mikesaelim.poleposition.service.TimeUtils.convertToUtcTimestamp;
import static java.util.stream.Collectors.toList;

@Service
public class ArticleLookupServiceImpl implements ArticleLookupService {

    @Autowired
    private ArticlePersistenceRepository repository;
    @Autowired
    private ArticleMapper mapper;
    @Autowired
    private AcceptanceWindowCalculator acceptanceWindowCalculator;

    @Override
    public ArticleMetadata retrieveRecord(@NonNull String identifier) {
        ArticlePersistence persistence = repository.findOne(identifier);

        if (persistence == null) {
            return null;
        }

        return mapper.fromPersistence(persistence);
    }

    @Override
    public List<ArticleMetadata> retrieveRecordsFor(@NonNull String primaryCategory, @NonNull LocalDate day)
            throws NoAcceptanceWindowException {
        AcceptanceWindow acceptanceWindow = acceptanceWindowCalculator.acceptanceWindowFor(day);
        if (acceptanceWindow == null) {
            throw new NoAcceptanceWindowException("No acceptance window starting on " + day.toString());
        }

        return repository.findByPrimaryCategoryAndSubmissionTime(primaryCategory,
                convertToUtcTimestamp(acceptanceWindow.getStart()), convertToUtcTimestamp(acceptanceWindow.getEnd()))
                .stream()
                .map(mapper::fromPersistence)
                .collect(toList());
    }

}
