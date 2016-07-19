package io.github.mikesaelim.poleposition.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePersistenceRepository extends JpaRepository<ArticlePersistence, String> {
}
