PolePosition
============

This project is a deployable web service which pulls records from the arXiv preprint repository (using the 
award-winning [arXiv OAI Harvester library](https://github.com/mikesaelim/arXivOAIHarvester)) and stores the metadata 
in a MySQL database, on command.  Records are then retrievable from MySQL by their identifier.

Eventually, this web service will be transformed to perform some mysterious function... mwahaha...

### How to run:

Unfortunately, right now the artifact for the [arXiv OAI Harvester](https://github.com/mikesaelim/arXivOAIHarvester) 
library isn't published anywhere, so you'll need to publish it locally.  Once you've cloned it, follow the instructions
in that project's README.md to do that.

Coming back to this project, you'll next need to let the app know where your MySQL database is.  Change the settings in 
the `application.yml` and `build.gradle` (under the "flyway" closure) files to point to your database.

Optionally, change the configuration in `application.yml` for how you want the harvester to hit the arXiv OAI 
repository.

Then use Flyway to provision your database, with

    ./gradlew flywayMigrate

Then run it with

    ./gradlew clean bootRun
    
which will deploy it to localhost:8080.

### Ingestion operations:

To ingest a single record by its OAI identifier (say, `oai:arXiv.org:1302.2146`),

    PUT http://localhost:8080/records/oai:arXiv.org:1302.2146
    
To ingest all the records in a certain set (say, `physics:hep-ph`) and since a certain date,

    PUT http://localhost:8080/records?from=2016-07-20&set=physics:hep-ph
    
### Lookup operations:

To look up a single record by its OAI identifier after you've ingested it,

    GET http://localhost:8080/records/oai:arXiv.org:1302.2146

To look up the records submitted during an acceptance window starting on a certain day, for a certain primary category, 
    
    GET http://localhost:8080/records?category=hep-ph&day=2016-08-22
    
To look up the earliest submitted record during an acceptance window starting on a certain day, for a certain primary
category,

    GET http://localhost:8080/records/first?category=hep-ph&day=2016-08-22
    