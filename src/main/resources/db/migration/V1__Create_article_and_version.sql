CREATE TABLE article (
  identifier VARCHAR(255) NOT NULL,
  retrieval_date_time_utc DATETIME NOT NULL,
  datestamp DATE NOT NULL,
  sets VARCHAR(255),
  deleted TINYINT(1) NOT NULL,
  id VARCHAR(255) NOT NULL,
  submitter VARCHAR(255) NOT NULL,
  title BLOB NOT NULL,
  authors BLOB NOT NULL,
  categories VARCHAR(255) NOT NULL,
  comments BLOB,
  proxy VARCHAR(255),
  report_no VARCHAR(255),
  acm_class VARCHAR(255),
  msc_class VARCHAR(255),
  journal_ref VARCHAR(255),
  doi VARCHAR(255),
  license VARCHAR(255),
  article_abstract BLOB,
  PRIMARY KEY (identifier)
);

CREATE TABLE version (
  identifier VARCHAR(255) NOT NULL,
  version_number TINYINT UNSIGNED NOT NULL,
  submission_time_utc DATETIME NOT NULL,
  size VARCHAR(255),
  source_type VARCHAR(255),
  PRIMARY KEY (identifier, version_number)
);