package io.github.mikesaelim.poleposition.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ArticlePersistenceRepository extends JpaRepository<ArticlePersistence, String> {

    @Query("SELECT a FROM ArticlePersistence a WHERE a.primaryCategory = :primaryCategory " +
            "AND a.submissionTimeUtc BETWEEN :startTime AND :endTime")
    List<ArticlePersistence> findByPrimaryCategoryAndSubmissionTime(@Param("primaryCategory") String primaryCategory,
                                                                    @Param("startTime") Timestamp start,
                                                                    @Param("endTime") Timestamp end);

}
