

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.XMLFormatter;


public class XMLFormatterDate {

    static final class TimeStamp {

        final ZonedDateTime zdt;
        TimeStamp(ZoneId zoneId) {
            zdt = ZonedDateTime.now(zoneId);
        }
        int getYear() {
            return zdt.getYear();
        }
        boolean isJanuaryFirst() {
            return zdt.getMonth() == Month.JANUARY && zdt.getDayOfMonth() == 1;
        }
    }


    
    public static void main(String[] args) {
        Locale locale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.ENGLISH);

            
            System.out.println("Testing with UTC");
            test(() -> new TimeStamp(ZoneOffset.UTC));

            
            
            try {
                Properties props = new Properties();
                props.setProperty("java.util.logging.XMLFormatter.useInstant", "false");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                props.store(baos, "");
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                LogManager.getLogManager().updateConfiguration(bais, (k) -> (o,n) -> n!=null?n:o);
            } catch (IOException io) {
                throw new RuntimeException(io);
            }

            
            System.out.println("Testing with old format");
            test(() -> new TimeStamp(ZoneId.systemDefault()));

        } finally {
            Locale.setDefault(locale);
        }
    }

    static void test(Supplier<TimeStamp> timeStampSupplier) {

        TimeStamp t1 = timeStampSupplier.get();
        int year1 = t1.getYear();

        LogRecord record = new LogRecord(Level.INFO, "test");
        XMLFormatter formatter = new XMLFormatter();
        final String formatted = formatter.format(record);
        System.out.println(formatted);

        final TimeStamp t2 = timeStampSupplier.get();
        final int year2 = t2.getYear();
        if (year2 < 1900) {
            throw new Error("Invalid system year: " + year2);
        }

        final StringBuilder buf2 = new StringBuilder()
                .append("<date>").append(year2).append("-");
        if (!formatted.contains(buf2.toString())) {
            StringBuilder buf1 = new StringBuilder()
                    .append("<date>").append(year1).append("-");
            if (formatted.contains(buf1) && year2 == year1 + 1
                    && t2.isJanuaryFirst()) {
                
                System.out.println("Happy new year!");
            } else {
                throw new Error("Expected year " + year2
                        + " not found in log:\n" + formatted);
            }
        }
    }

}
