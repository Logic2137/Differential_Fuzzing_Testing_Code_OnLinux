
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;


public class LogRecordWithNanos {

    static final int MILLIS_IN_SECOND = 1000;
    static final int NANOS_IN_MILLI = 1000_000;
    static final int NANOS_IN_SECOND = 1000_000_000;

    static final boolean verbose = false;

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

    
    public static void test(LogRecord record)
            throws IOException, ClassNotFoundException {

        
        SimpleFormatter formatter = new SimpleFormatter();
        String str = formatter.format(record);

        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(record);
        oos.flush();
        oos.close();

        
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
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
        assertEquals(record.getMillis(),
                record.getInstant().toEpochMilli(),
                "getMillis()/getInstant().toEpochMilli()");
        assertEquals(record2.getMillis(),
                record2.getInstant().toEpochMilli(),
                "getMillis()/getInstant().toEpochMilli()");
        assertEquals((record.getMillis()%MILLIS_IN_SECOND)*NANOS_IN_MILLI
                + (record.getInstant().getNano() % NANOS_IN_MILLI),
                record.getInstant().getNano(),
                "record.getMillis()%1000)*1000_000"
                + " + record.getInstant().getNano()%1000_000 / getInstant().getNano()");
        assertEquals((record2.getMillis()%MILLIS_IN_SECOND)*NANOS_IN_MILLI
                + (record2.getInstant().getNano() % NANOS_IN_MILLI),
                record2.getInstant().getNano(),
                "record2.getMillis()%1000)*1000_000"
                + " + record2.getInstant().getNano()%1000_000 / getInstant().getNano()");

        
        
        
        String str2 = formatter.format(record2);
        if (!str.equals(str2))
            throw new RuntimeException("Unexpected values in deserialized object:"
                + "\n\tExpected:  " + str
                + "\n\tRetrieved: "+str);

    }


    public static void main(String[] args) throws Exception {
        int count=0;
        for (int i=0; i<1000; i++) {
            LogRecord record = new LogRecord(Level.INFO, "Java Version: {0}");
            record.setLoggerName("test");
            record.setParameters(new Object[] {System.getProperty("java.version")});
            if (record.getInstant().getNano() % 1000_000 != 0) {
                count++;
            }
            test(record);
        }
        if (count == 0) {
            throw new RuntimeException("Expected at least one record to have nanos");
        }
        System.out.println(count + "/1000 records had nano adjustment.");
    }

}
