
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


public class LogRecordWithNanosAPI {

    static final int MILLIS_IN_SECOND = 1000;
    static final int NANOS_IN_MILLI = 1000_000;
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

    private static void assertEquals(Object expected, Object received, String msg) {
        if (!Objects.equals(expected, received)) {
            throw new TestAssertException("Unexpected result for " + msg
                    + ".\n\texpected: " + expected
                    +  "\n\tactual:   " + received);
        } else if (verbose) {
            System.out.println("Got expected " + msg + ": " + received);
        }
    }

    
    
    private static long nanoInSecondFromEpochMilli(long millis) {
        return (((millis%MILLIS_IN_SECOND) + MILLIS_IN_SECOND)%MILLIS_IN_SECOND)*NANOS_IN_MILLI;
    }

    
    public static void test(LogRecord record, boolean hasExceedingNanos)
            throws IOException, ClassNotFoundException {

        
        SimpleFormatter formatter = new SimpleFormatter();
        String str = formatter.format(record);

        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(record);
        oos.flush();
        oos.close();

        
        final ByteArrayInputStream bais =
                new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(bais);
        final LogRecord record2 = (LogRecord)ois.readObject();

        assertEquals(record.getMillis(), record2.getMillis(), "getMillis()");
        assertEquals(record.getInstant().getEpochSecond(),
                record2.getInstant().getEpochSecond(),
                "getInstant().getEpochSecond()");
        assertEquals(record.getInstant().getNano(),
                record2.getInstant().getNano(),
                "getInstant().getNano()");
        assertEquals(record.getInstant().toEpochMilli(),
                record2.getInstant().toEpochMilli(),
                "getInstant().toEpochMilli()");
        long millis = record.getMillis();
        millis = hasExceedingNanos
                ? Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                        (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI
                        + record.getInstant().getNano() % NANOS_IN_MILLI).toEpochMilli()
                : millis;
        assertEquals(millis, record.getInstant().toEpochMilli(),
                "getMillis()/getInstant().toEpochMilli()");
        millis = record2.getMillis();
        millis = hasExceedingNanos
                ? Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                        (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI
                        + record2.getInstant().getNano() % NANOS_IN_MILLI).toEpochMilli()
                : millis;
        assertEquals(millis, record2.getInstant().toEpochMilli(),
                "getMillis()/getInstant().toEpochMilli()");
        long nanos = nanoInSecondFromEpochMilli(record.getMillis())
                + record.getInstant().getNano() % NANOS_IN_MILLI;
        assertEquals(nanos, record.getInstant().getNano(),
                "nanoInSecondFromEpochMilli(record.getMillis())"
                + " + record.getInstant().getNano() % NANOS_IN_MILLI /getInstant().getNano()");
        nanos = nanoInSecondFromEpochMilli(record2.getMillis())
                + record2.getInstant().getNano() % NANOS_IN_MILLI;
        assertEquals(nanos, record2.getInstant().getNano(),
                "nanoInSecondFromEpochMilli(record2.getMillis())"
                + " + record2.getInstant().getNano() % NANOS_IN_MILLI /getInstant().getNano()");

        
        
        
        String str2 = formatter.format(record2);
        if (!str.equals(str2))
            throw new RuntimeException("Unexpected values in deserialized object:"
                + "\n\tExpected:  " + str
                + "\n\tRetrieved: "+str);

    }


    public static void main(String[] args) throws Exception {
        int count=0;
        LogRecord record = new LogRecord(Level.INFO, "Java Version: {0}");
        record.setLoggerName("test");
        record.setParameters(new Object[] {System.getProperty("java.version")});
        final int nanos = record.getInstant().getNano() % NANOS_IN_MILLI;
        final long millis = record.getMillis();
        final Instant instant = record.getInstant();
        if (millis != instant.toEpochMilli()) {
            throw new RuntimeException("Unexpected millis: "
                    + record.getMillis());
        }
        test(record, false);

        
        int newNanos = (nanos + 111111) % NANOS_IN_MILLI;
        record.setInstant(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + newNanos));
        assertEquals(newNanos, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis, record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newNanos),
                record.getInstant(), "record.getInstant()");
        test(record, false);
        assertEquals(newNanos, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis, record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newNanos),
                record.getInstant(), "record.getInstant()");

        
        int newExceedingNanos = 2111_111;
        record.setInstant(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + newExceedingNanos));
        assertEquals(newExceedingNanos % NANOS_IN_MILLI,
                record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis + newExceedingNanos / NANOS_IN_MILLI,
                record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");
        test(record, true);
        assertEquals(newExceedingNanos % NANOS_IN_MILLI,
                record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis  + newExceedingNanos / NANOS_IN_MILLI,
                record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");

        
        newExceedingNanos = 1111_111_111;
        record.setInstant(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + newExceedingNanos));
        assertEquals(newExceedingNanos % NANOS_IN_MILLI,
                record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis  + newExceedingNanos / NANOS_IN_MILLI,
                record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");
        test(record, true);
        assertEquals(newExceedingNanos % NANOS_IN_MILLI,
                record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis  + newExceedingNanos / NANOS_IN_MILLI,
                record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");

        
        newExceedingNanos = -1;
        record.setInstant(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + newExceedingNanos));
        assertEquals(newExceedingNanos + NANOS_IN_MILLI,
                record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        assertEquals(millis -1, record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");
        test(record, true);
        record.setInstant(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis % MILLIS_IN_SECOND) * NANOS_IN_MILLI + newExceedingNanos));
        assertEquals(millis -1, record.getMillis(), "record.getMillis()");
        assertEquals(Instant.ofEpochSecond(millis/MILLIS_IN_SECOND,
                (millis%MILLIS_IN_SECOND)*NANOS_IN_MILLI + newExceedingNanos),
                record.getInstant(), "record.getInstant()");

        
        record.setMillis(millis-1);
        assertEquals(millis-1, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(millis-1, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        test(record, false);
        assertEquals(millis-1, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(millis-1, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");

        
        record.setMillis(0);
        assertEquals(0, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(0, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        test(record, false);
        assertEquals(0, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(0, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");

        
        record.setMillis(-1);
        assertEquals(-1, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(-1, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");
        test(record, false);
        assertEquals(-1, record.getInstant().toEpochMilli(),
                "record.getInstant().toEpochMilli()");
        assertEquals(-1, record.getMillis(),
                "record.getMillis()");
        assertEquals(0, record.getInstant().getNano() % NANOS_IN_MILLI,
                "record.getInstant().getNano() % NANOS_IN_MILLI");

        try {
            record.setInstant(null);
            throw new RuntimeException("Expected NullPointerException not thrown");
        } catch (NullPointerException x) {
            System.out.println("Got expected NPE when trying to call record.setInstant(null): " + x);
        }

        
        final Instant max = Instant.ofEpochMilli(Long.MAX_VALUE).plusNanos(999_999L);
        record.setInstant(max);
        assertEquals(Long.MAX_VALUE / 1000L,
                     record.getInstant().getEpochSecond(),
                     "max instant seconds [record.getInstant().getEpochSecond()]");
        assertEquals(Long.MAX_VALUE,
                     record.getInstant().toEpochMilli(),
                     "max instant millis [record.getInstant().toEpochMilli()]");
        assertEquals(Long.MAX_VALUE, record.getMillis(),
                     "max instant millis [record.getMillis()]");
        assertEquals((Long.MAX_VALUE % 1000L)*1000_000L + 999_999L,
                     record.getInstant().getNano(),
                     "max instant nanos [record.getInstant().getNano()]");

        
        final Instant tooBig = max.plusNanos(1L);
        try {
            record.setInstant(tooBig);
            throw new RuntimeException("Expected ArithmeticException not thrown");
        } catch (ArithmeticException x) {
            System.out.println("Got expected ArithmeticException when trying"
                    + " to call record.setInstant(Instant.ofEpochMilli(Long.MAX_VALUE)"
                    + ".plusNanos(999_999L).plusNanos(1L)): " + x);
        }

    }

}
