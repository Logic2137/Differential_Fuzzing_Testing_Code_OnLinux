

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.Arrays;
import java.util.List;

public class Bug8149452 {

    public static void main(String[] args) {

        List<String> listNotFound = new ArrayList<>();
        String[][] zoneStrings = DateFormatSymbols.getInstance()
                .getZoneStrings();
        for (String tzID : TimeZone.getAvailableIDs()) {
            if (!Arrays.stream(zoneStrings)
                    .anyMatch(zone -> tzID.equalsIgnoreCase(zone[0]))) {
                
                
                if (!tzID.startsWith("Etc/GMT")
                        && !tzID.startsWith("GMT")
                        && !TimeZone.getTimeZone(tzID).getDisplayName().startsWith("GMT")) {
                    listNotFound.add(tzID);
                }
            }
        }

        if (!listNotFound.isEmpty()) {
            throw new RuntimeException("Test Failed: Time Zone Strings for "
                    + listNotFound + " not found");
        }

    }

}
