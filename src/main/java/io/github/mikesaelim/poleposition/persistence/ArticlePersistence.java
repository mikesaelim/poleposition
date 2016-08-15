package io.github.mikesaelim.poleposition.persistence;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;

/**
 * Persistence object for metadata about an article.  See {@link ArticleMetadata} for more info.
 */
@Entity
@Table(name = "article")
@Data
public class ArticlePersistence {

    /**
     * OAI identifier of the record.  Primary key.
     */
    @Id
    @Column(name = "identifier", nullable = false)
    private String identifier;

    /**
     * Primary category that the record was submitted under.  This is duplicated information corresponding to the first
     * element of the "categories" list, in order to facilitate easier queries.  Other elements in the "categories" list
     * are the cross-listed categories.
     */
    @Column(name = "primary_category", nullable = false)
    private String primaryCategory;

    /**
     * Submission time, in UTC, of the first version of the article.  This is duplicated information corresponding to
     * the submission time of the article version with version number 1, in order to facilitate easier queries.
     */
    @Column(name = "submission_time_utc", nullable = false)
    private Timestamp submissionTimeUtc;

    @Column(name = "retrieval_date_time_utc", nullable = false)
    private Timestamp retrievalDateTimeUtc;

    @Column(name = "datestamp", nullable = false)
    private Date datestamp;

    /**
     * Sets serialized into a comma-separated list
     */
    @Column(name = "sets")
    private String sets;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "submitter", nullable = false)
    private String submitter;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "authors", nullable = false)
    private String authors;

    /**
     * Categories serialized into a comma-separated list
     */
    @Column(name = "categories", nullable = false)
    private String categories;

    @Column(name = "comments")
    private String comments;

    @Column(name = "proxy")
    private String proxy;

    @Column(name = "report_no")
    private String reportNo;

    @Column(name = "acm_class")
    private String acmClass;

    @Column(name = "msc_class")
    private String mscClass;

    @Column(name = "journal_ref")
    private String journalRef;

    @Column(name = "doi")
    private String doi;

    @Column(name = "license")
    private String license;

    @Column(name = "article_abstract")
    private String articleAbstract;

    /**
     * An article can have multiple versions.
     */
    @OneToMany(mappedBy = "identifier", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ArticleVersionPersistence> versions;

}
