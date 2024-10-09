import java.io.File;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

public class TimeZoneLastModified {

    private static final long TIME = 1023199010002L;

    public static void main(String[] args) throws Throwable {
        int failures = test(null);
        for (String timeZoneID : TimeZone.getAvailableIDs()) {
            failures += test(timeZoneID);
        }
        if (failures != 0) {
            throw new RuntimeException("TimeZoneLastModified failed");
        }
        System.out.println("TimeZoneLastModified passed");
    }

    private static int test(String timeZoneID) throws Throwable {
        File f = new File("test-timezone.txt");
        int failures = 0;
        try {
            f.createNewFile();
            if (timeZoneID != null) {
                TimeZone.setDefault(TimeZone.getTimeZone(timeZoneID));
            }
            boolean succeeded = f.setLastModified(TIME);
            if (!succeeded) {
                System.err.format("Setting time to %d failed for time zone %s%n", TIME, timeZoneID);
                failures++;
            }
            long time = f.lastModified();
            if (Math.abs(time - TIME) > 999) {
                System.err.format("Wrong modification time (ms): expected %d, obtained %d%n", TIME, time);
                failures++;
            }
        } finally {
            f.delete();
        }
        return failures;
    }
}
