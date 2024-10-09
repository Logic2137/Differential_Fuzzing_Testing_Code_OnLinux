


import java.sql.Timestamp;
import java.util.Date;

public class Bug8135055 {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Date d = new Date();
            Timestamp ts = new Timestamp(d.getTime());
            if (d.after(ts)) {
                throw new RuntimeException("date with time " + d.getTime()
                        + " should not be after TimeStamp , Nanos component of "
                                + "TimeStamp is " +ts.getNanos());
            }
            Thread.sleep(1);
        }
    }
}
