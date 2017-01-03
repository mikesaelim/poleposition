package io.github.mikesaelim.poleposition.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ArticlePersistenceRepository extends JpaRepository<ArticlePersistence, String> {

    /**
     * Retrieve a list of entries with the given primary category and an original submission time in a certain range.
     * The returned list is sorted in ascending order by the submission time.
     *
     * This will return an empty list if any parameters are null.  See https://jira.spring.io/browse/DATAJPA-209
     */
    @Query("SELECT a FROM ArticlePersistence a WHERE a.primaryCategory = :primaryCategory " +
            "AND a.submissionTimeUtc BETWEEN :startTime AND :endTime ORDER BY a.submissionTimeUtc")
    List<ArticlePersistence> findByPrimaryCategoryAndSubmissionTime(@Param("primaryCategory") String primaryCategory,
                                                                    @Param("startTime") Timestamp start,
                                                                    @Param("endTime") Timestamp end);

}
