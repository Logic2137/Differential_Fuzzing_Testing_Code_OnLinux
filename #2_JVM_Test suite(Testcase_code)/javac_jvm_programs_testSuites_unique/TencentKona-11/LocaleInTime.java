



import java.util.Date;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class LocaleInTime {
    public static void main(String args[]) throws Exception {
        DerOutputStream out = new DerOutputStream();
        out.putUTCTime(new Date());
        DerValue val = new DerValue(out.toByteArray());
        System.out.println(val.getUTCTime());
    }
}
