
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


public class SimpleFormatterNanos {

    static final int MILLIS_IN_SECOND = 1000;
    static final int NANOS_IN_MILLI = 1000_000;
    static final int NANOS_IN_MICRO = 1000;
    static final int NANOS_IN_SECOND = 1000_000_000;

    static final boolean verbose = true;

    static final class TestAssertException extends RuntimeException {
        TestAssertException(String msg) { super(msg); }
    }

    private static void assertEquals(long expected, long received, String msg) {
        if (expected != received) {
            throw new TestAssertException("Unexpected result for " + msg
                    + ".\n\texpected: " + expected
                    +  "\n\tactual:   " + received);
        } else if (verbose) {
            System.out.println("Got expected " + msg + ": " + received);
        }
    }

    static int getNanoAdjustment(LogRecord record) {
        return record.getInstant().getNano() % NANOS_IN_MILLI;
    }
    static void setNanoAdjustment(LogRecord record, int nanos) {
        record.setInstant(Instant.ofEpochSecond(record.getInstant().getEpochSecond(),
                (record.getInstant().getNano() / NANOS_IN_MILLI) * NANOS_IN_MILLI + nanos));
    }

    public static void main(String[] args) throws Exception {

        Locale.setDefault(Locale.ENGLISH);
        LogRecord record = new LogRecord(Level.INFO, "Java Version: {0}");
        record.setLoggerName("test");
        record.setParameters(new Object[] {System.getProperty("java.version")});
        int nanos = getNanoAdjustment(record);
        long millis = record.getMillis();
        
        
        if (millis % MILLIS_IN_SECOND < 100) millis = millis + 100;
        
        if (nanos % NANOS_IN_MICRO == 0) nanos = nanos + 999;
        record.setMillis(millis);
        setNanoAdjustment(record, nanos);
        final Instant instant = record.getInstant();
        if (nanos < 0) {
            throw new RuntimeException("Unexpected negative nano adjustment: "
                    + getNanoAdjustment(record));
        }
        if (nanos >= NANOS_IN_MILLI) {
            throw new RuntimeException("Nano adjustment exceeds 1ms: "
                    + getNanoAdjustment(record));
        }
        if (millis != record.getMillis()) {
            throw new RuntimeException("Unexpected millis: " + millis + " != "
                    + record.getMillis());
        }
        if (millis != record.getInstant().toEpochMilli()) {
            throw new RuntimeException("Unexpected millis: "
                    + record.getInstant().toEpochMilli());
        }
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        int zdtNanos = zdt.getNano();
        long expectedNanos = (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + nanos;
        assertEquals(expectedNanos, instant.getNano(), "Instant.getNano()");
        assertEquals(expectedNanos, zdtNanos, "ZonedDateTime.getNano()");
        String match = "."+expectedNanos+" ";

        System.out.println("Testing with default format...");

        SimpleFormatter formatter = new SimpleFormatter();
        String str = formatter.format(record);
        if (str.contains(match)) {
            throw new RuntimeException("SimpleFormatter.format()"
                    + " string contains unexpected nanos: "
                    + "\n\tdid not expect match for: '" + match + "'"
                    + "\n\tin: " + str);
        }

        System.out.println("Nanos omitted as expected: no '"+match+"' in "+str);


        System.out.println("Changing format to print nanos...");
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tN %1$Tp %2$s%n%4$s: %5$s%6$s%n");

        SimpleFormatter formatter2 = new SimpleFormatter();
        str = formatter2.format(record);
        if (!str.contains(match)) {
            throw new RuntimeException("SimpleFormatter.format()"
                    + " string does not contain expected nanos: "
                    + "\n\texpected match for: '" + match + "'"
                    + "\n\tin: " + str);
        }
        System.out.println("Found expected match for '"+match+"' in "+str);


        System.out.println("Changing format to omit nanos...");
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n");

        SimpleFormatter formatter3 = new SimpleFormatter();
        str = formatter3.format(record);
        String notMatch = match;
        if (str.contains(notMatch)) {
            throw new RuntimeException("SimpleFormatter.format()"
                    + " string contains unexpected nanos: "
                    + "\n\tdid not expect match for: '" + notMatch + "'"
                    + "\n\tin: " + str);
        }
        System.out.println("Nanos omitted as expected: no '"+notMatch+"' in "+str);

    }

}
