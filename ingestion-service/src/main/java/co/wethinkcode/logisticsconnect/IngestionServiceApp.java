package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.io.*;

public class IngestionServiceApp {

    public static void main(String[] args) throws FileNotFoundException {
        Javalin app = Javalin.create().start(7050);

        app.get("/health", ctx -> ctx.result("OK"));

        // TODO: read and clean src/main/resources/hubs-global.csv (hubs, sorting centers, regional districts data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.
//        File file = new File("ingestion-service/src/main/resources/hubs-global.csv");
//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.exists());

        readCsv();

    }
    public static void readCsv(){
        try {BufferedReader reader = new BufferedReader(new FileReader("ingestion-service/src/main/resources/hubs-global.csv"));
        // reading the header
        String line =reader.readLine();

        line = reader.readLine();


        while (line != null) {
            String [] columns = line.split(",");

            String hubId = cleanHubId(columns[0]);
            String province = cleanProvince(columns[1]);
            String sortingCentre = cleanSortingCenter(columns[2]);
            String activeStatus = cleanActiveStatus(columns[3]);


            line = reader.readLine();



        }
    } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static String cleanHubId(String hubId) {
        String cleaningHub = hubId.trim().replaceAll("\\s+", " ");

        char firstCharacter = cleaningHub.charAt(0);
        firstCharacter = Character.toUpperCase(firstCharacter);
        String cleanedHub = firstCharacter + cleaningHub;
        return cleanedHub;
    }

    public static String cleanProvince(String province) {
        if (province == null) {
            return null;
        }

        String cleanedProvince = province.trim().replaceAll("\\s+", " ").toUpperCase();

        if (cleanedProvince.equals("KWAZULU-NATAL") || cleanedProvince.equals("KWA-ZULU NATAL") || cleanedProvince.equals("KWAZULU NATAL")) {
            cleanedProvince = "KWAZULU-NATAL";
        }

        return cleanedProvince;
    }

    public static String cleanSortingCenter(String sortingCenter) {
        String cleanedSortingCenter = sortingCenter.trim().replaceAll("\\s+", " ");
        return cleanedSortingCenter;
    }

    public static String cleanActiveStatus(String activeStatus) {
        return activeStatus;
    }
}
