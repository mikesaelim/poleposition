package io.github.mikesaelim.poleposition.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticlePersistenceRepository extends JpaRepository<ArticlePersistence, String> {
}
