package co.wethinkcode.logisticsconnect;

import io.javalin.Javalin;

import java.io.*;
import java.util.ArrayList;

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
    public static ArrayList<Hub> readCsv() {
        ArrayList<Hub> cleanedHubs = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("ingestion-service/src/main/resources/hubs-global.csv"));
            // reading the header
            String line = reader.readLine();

            line = reader.readLine();


            while (line != null) {
                String[] columns = line.split(",");

                String hubId = cleanHubId(columns[0]);
                String province = cleanProvince(columns[1]);
                String sortingCenter = cleanSortingCenter(columns[2]);
                String activeStatus = cleanActiveStatus(columns[3]);


                Hub hub = new Hub(hubId, province, sortingCenter, activeStatus);

                cleanedHubs.add(hub);

                line = reader.readLine();


            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }return cleanedHubs;
    }

    public static String cleanHubId(String hubId) {
        if (hubId == null) {
            return null;
        }

        String cleaningHubId = hubId.trim().replaceAll("\\s+", " ");

        char firstCharacter = cleaningHubId.charAt(0);
        firstCharacter = Character.toUpperCase(firstCharacter);
        String cleanedHubId = firstCharacter + cleaningHubId.substring(1);
        return cleanedHubId;
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
        if (sortingCenter == null) {
            return null;
        }
        String cleanedSortingCenter = sortingCenter.trim().replaceAll("\\s+", " ").toUpperCase();
        return cleanedSortingCenter;
    }

    public static String cleanActiveStatus(String activeStatus) {
        if (activeStatus == null) {
            return null;
        }
        String cleaned = activeStatus.trim().replaceAll("\\s+", " ").toLowerCase();

        if (cleaned.equals("1") || cleaned.equals("y") || cleaned.equals("yes") || cleaned.equals("true") || cleaned.equals("active")) {
            cleaned = "Active";
        }
        else if (cleaned.equals("0") || cleaned.equals("no") || cleaned.equals("false") || cleaned.equals("n")) {
            cleaned = "Inactive";
        } else if (cleaned.equals("n/a")) {
            cleaned = "N/A";
        } else if (cleaned.equals("unknown")) {
            cleaned = "Unknown";
        }
        return cleaned;
    }


}
