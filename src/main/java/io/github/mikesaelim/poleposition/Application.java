package io.github.mikesaelim.poleposition;

import io.github.mikesaelim.poleposition.persistence.PersistenceConfig;
import io.github.mikesaelim.poleposition.service.ArxivIngestionService;
import io.github.mikesaelim.poleposition.service.ServiceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Scanner;

@Import({ServiceConfig.class, PersistenceConfig.class})
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    ArxivIngestionService arxivIngestionService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("Wanna (1) get a record or (2) list records since a given date?");

        switch (scanner.nextLine().trim()) {
            case "1":
                getRecord(scanner);
                break;
            case "2":
                listRecords(scanner);
        }
    }

    private void getRecord(Scanner scanner) throws Exception {
        System.out.println("Record id?");
        String identifier = scanner.nextLine().trim();

        arxivIngestionService.ingestRecord(identifier);
    }

    private void listRecords(Scanner scanner) throws Exception {
        System.out.println("From when?");
        String fromDateString = scanner.nextLine().trim();
        System.out.println("Set?");
        String setSpec = scanner.nextLine().trim();

        LocalDate fromDate = StringUtils.isNotBlank(fromDateString) ? LocalDate.parse(fromDateString) : null;

        arxivIngestionService.ingestMetadataSince(fromDate, setSpec);
    }

}
