package io.github.mikesaelim.poleposition.persistence;

import io.github.mikesaelim.arxivoaiharvester.model.data.ArticleMetadata;
import lombok.Builder;
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
@Builder
public class ArticlePersistence {

    @Id
    @Column(name = "identifier", nullable = false)
    private String identifier;

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
