

import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.function.Supplier;


public class SimpleTimeZoneCloneRaceTest {

    public static void main(String[] args) throws InterruptedException {

        
        
        TimeZoneUser sharedTZuser = new TimeZoneUser(() -> sharedTZ);

        
        
        TimeZoneUser clonedTZuser = new TimeZoneUser(() -> {
            
            TimeZone tz = sharedTZ;
            
            
            cpuHogTZ.getOffset(time);
            
            
            return (TimeZone) tz.clone();
        });

        
        Thread t1 = new Thread(sharedTZuser);
        Thread t2 = new Thread(clonedTZuser);
        t1.start();
        t2.start();

        
        long t0 = System.currentTimeMillis();
        do {
            TimeZone tz1 = createSTZ();
            TimeZone tz2 = createSTZ();
            cpuHogTZ = tz1;
            sharedTZ = tz2;
        } while (System.currentTimeMillis() - t0 < 2000L);

        sharedTZuser.stop = true;
        clonedTZuser.stop = true;
        t1.join();
        t2.join();

        System.out.println(
            String.format("shared TZ: %d correct, %d incorrect calculations",
                          sharedTZuser.correctCount, sharedTZuser.incorrectCount)
        );
        System.out.println(
            String.format("cloned TZ: %d correct, %d incorrect calculations",
                          clonedTZuser.correctCount, clonedTZuser.incorrectCount)
        );
        if (clonedTZuser.incorrectCount > 0) {
            throw new RuntimeException(clonedTZuser.incorrectCount +
                                       " fatal data races detected");
        }
    }

    static SimpleTimeZone createSTZ() {
        return new SimpleTimeZone(-28800000,
                                  "America/Los_Angeles",
                                  Calendar.APRIL, 1, -Calendar.SUNDAY,
                                  7200000,
                                  Calendar.OCTOBER, -1, Calendar.SUNDAY,
                                  7200000,
                                  3600000);
    }

    static volatile TimeZone cpuHogTZ = createSTZ();
    static volatile TimeZone sharedTZ = createSTZ();
    static final long time;
    static final long correctOffset;

    static {
        TimeZone tz = createSTZ();
        Calendar cal = Calendar.getInstance(tz, Locale.ROOT);
        cal.set(2000, Calendar.MAY, 1, 0, 0, 0);
        time = cal.getTimeInMillis();
        correctOffset = tz.getOffset(time);
    }

    static class TimeZoneUser implements Runnable {
        private final Supplier<? extends TimeZone> tzSupplier;

        TimeZoneUser(Supplier<? extends TimeZone> tzSupplier) {
            this.tzSupplier = tzSupplier;
        }

        volatile boolean stop;
        int correctCount, incorrectCount;

        @Override
        public void run() {
            while (!stop) {
                TimeZone tz = tzSupplier.get();
                int offset = tz.getOffset(time);
                if (offset == correctOffset) {
                    correctCount++;
                } else {
                    incorrectCount++;
                }
            }
        }
    }
}
