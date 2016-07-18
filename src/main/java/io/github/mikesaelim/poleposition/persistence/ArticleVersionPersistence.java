package io.github.mikesaelim.poleposition.persistence;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleVersion;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Persistence object for metadata about a version of an article.  See {@link ArticleVersion} for more info.
 */
@Entity
@Table(name = "article_version")
@Data
@Builder
public class ArticleVersionPersistence {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "submission_time_utc", nullable = false)
    private Timestamp submissionTimeUtc;

    @Column(name = "size")
    private String size;

    @Column(name = "source_type")
    private String sourceType;

}
