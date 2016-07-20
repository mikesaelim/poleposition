package io.github.mikesaelim.poleposition.service;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistence;
import io.github.mikesaelim.poleposition.persistence.ArticlePersistenceRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleLookupServiceImpl implements ArticleLookupService {

    @Autowired
    private ArticlePersistenceRepository repository;
    @Autowired
    private ArticleMapper mapper;

    @Override
    public ArticleMetadata retrieveRecord(@NonNull String identifier) {
        ArticlePersistence persistence = repository.findOne(identifier);

        if (persistence == null) {
            return null;
        }

        return mapper.fromPersistence(persistence);
    }

}
