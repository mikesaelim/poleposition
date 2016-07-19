PolePosition
============

This project is currently a command-line application which pulls records from the arXiv preprint repository (using the 
award-winning arXiv OAI Harvester library) and stores the metadata in a MySQL database.

Eventually, this project will be transformed into a web service performing some mysterious function...

### How to run:

You'll first need to let the app know where your MySQL database is.  Change the settings in the `application.yml` and
`build.gradle` (under the "flyway" closure) files to point to your database.

Then use Flyway to provision your database, with

    ./gradlew flywayMigrate

Then run it with

    ./gradlew clean build && java -jar build/libs/poleposition-0.0.1.jar